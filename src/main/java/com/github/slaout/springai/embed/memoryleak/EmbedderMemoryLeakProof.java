package com.github.slaout.springai.embed.memoryleak;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.github.slaout.springai.embed.memoryleak.RandomWordsGenerator.generateRandomWords;

@SpringBootApplication
public class EmbedderMemoryLeakProof implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    private final EmbeddingModel embeddingModel;

    public EmbedderMemoryLeakProof(
            ApplicationContext applicationContext,
            EmbeddingModel embeddingModel
    ) {
        this.applicationContext = applicationContext;
        this.embeddingModel = embeddingModel;
    }

    // Run with: -Xms1g -Xmx1g -XX:NativeMemoryTracking=summary
    public static void main(String[] args) {
        System.setProperty("ai.djl.pytorch.num_interop_threads", "4");
        System.setProperty("ai.djl.pytorch.num_threads", "4");

        SpringApplication.run(EmbedderMemoryLeakProof.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        int embeddingThreads = 4; // Same with 1, but slower
        Thread[] threads = new Thread[embeddingThreads + 1];

        for (int i = 0; i < embeddingThreads; i++) {
            threads[i] = new Thread(this::embedALot);
            threads[i].start();
        }

        threads[threads.length - 1] = new Thread(this::showRamUsageEverySecond);
        threads[threads.length - 1].start();

        for (Thread thread : threads) {
            thread.join();
        }

        SpringApplication.exit(applicationContext, () -> 0);
    }

    private void embedALot() {
        while (true) {
            embeddingModel.embed(generateRandomWords(5));
        }
    }

    private void showRamUsageEverySecond() {
        while (true) {
            try {
                showRamUsage();
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                return;
            }
        }
    }

    private void showRamUsage() throws IOException {
        long pid = ProcessHandle.current().pid();
        Runtime runtime = Runtime.getRuntime();

        String[] cmd = {"/bin/sh", "-c", "ps -o vsz= -p " + pid};
        Process process = runtime.exec(cmd);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            System.out.println(
                    // Always 1 073 741 824 = 1 GB, as defined in "-Xms1g -Xmx1g":
                    // runtime.totalMemory() + "\t" +

                    // Follows a normal sawtooth-graph like any healthy JVM with regular GCs:
                    // (runtime.totalMemory() - runtime.freeMemory()) + "\t" +

                    // Always increases:
                    reader.readLine().trim());
        }
    }

}
