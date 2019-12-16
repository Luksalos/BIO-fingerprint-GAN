import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {
    public void run(String fakeImagePath, String realImagesDir, Integer count) throws Exception {
        List<Fingerprint> fakes = loadImages(fakeImagePath);
        List<Fingerprint> originals = loadImages(realImagesDir);

        for (Fingerprint fake : fakes) {
            printMatches(fake.imagePath, findClosestMatches(fake.template, originals, count));
        }
    }

    private List<Pair<String, Double>> findClosestMatches(FingerprintTemplate probe, List<Fingerprint> candidates, Integer count) {
        FingerprintMatcher matcher = new FingerprintMatcher().index(probe);

        List<Pair<String, Double>> matches = new ArrayList<>();

        for (Fingerprint candidate : candidates) {
            double score = matcher.match(candidate.template);
            matches.add(new Pair<>(candidate.imagePath, score));
        }

        matches.sort((a, b) -> - a.getValue1().compareTo(b.getValue1()));

        return matches.subList(0, count);
    }

    private List<Fingerprint> loadImages(String imagesDir) throws IOException {
        ArrayList<Fingerprint> fingerprints = new ArrayList<>();

        File[] images = new File(imagesDir).listFiles();

        if (images == null) {
            throw new IOException("Could not list files in " + imagesDir);
        }

        for (File image : images) {
            String imagePath = image.getAbsolutePath();

            try {
                FingerprintTemplate template = createTemplate(imagePath);
                fingerprints.add(new Fingerprint(imagePath, template));
            } catch (IOException e) {
                System.err.println("Could not open " + imagePath);
            }
        }

        return fingerprints;
    }

    private FingerprintTemplate createTemplate(String imagePath) throws IOException {
        byte[] image = Files.readAllBytes(Paths.get(imagePath));
        return new FingerprintTemplate(new FingerprintImage().dpi(500).decode(image));
    }

    private void printMatches(String fakeImagePath, List<Pair<String, Double>> closestMatches) {
        System.out.println("Closest " + closestMatches.size() + " matches for " + fakeImagePath + ":");
        for (Pair<String, Double> match : closestMatches) {
            System.out.println("  * " + match.getValue0() + "\t" + match.getValue1());
        }
    }

    private static class Fingerprint {
        public String imagePath;
        public FingerprintTemplate template;

        public Fingerprint(String imagePath, FingerprintTemplate template) {
            this.imagePath = imagePath;
            this.template = template;
        }
    }

    public static void main(String[] arguments) {
        if (arguments.length != 3) {
            System.err.println("Arguments: <fake_fingerprints:dir> <real_fingerprints:dir> <top_n_matches:int>");
            return;
        }

        try {
            new App().run(arguments[0], arguments[1], Integer.parseInt(arguments[2]));
        } catch (Exception e) {
            System.err.println("An internal error occurred.");
        }
    }
}
