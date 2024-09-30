package com.github.slaout.springai.embed.memoryleak;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class EmbedderMemoryLeakProof implements CommandLineRunner {

    private final EmbeddingModel embeddingModel;

    public EmbedderMemoryLeakProof(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    // Run with: -Xms1g -Xmx1g -XX:NativeMemoryTracking=summary
    public static void main(String[] args) {
        SpringApplication.run(EmbedderMemoryLeakProof.class, args);
    }

    @Override
    public void run(String... args) {
        new Thread(this::embedALot).start();
        new Thread(this::showRamUsageEverySecond).start();
    }

    private void embedALot() {
        while (true) {
            embeddingModel.embed("some text to embed");
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
