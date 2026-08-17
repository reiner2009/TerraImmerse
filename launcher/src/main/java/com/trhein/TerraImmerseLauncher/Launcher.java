package com.trhein.TerraImmerseLauncher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Launcher {

    private static final String BASE_URL =
            "http://terraimmerse.t-rhein.com:8080/";

    private static final String RELEASES_URL =
            BASE_URL + "resources/releases.json";

    private static final Path GAME_DIRECTORY =
            Paths.get(System.getProperty("user.home"), ".terraimmerse");

    private static final Path VERSIONS_DIRECTORY =
            GAME_DIRECTORY.resolve("versions");

    private static final Path NATIVES_DIRECTORY =
            GAME_DIRECTORY.resolve("natives");

    private final JFrame frame;

    private final JComboBox<Release> versionBox;
    private final JRadioButton fullEdition;
    private final JRadioButton piEdition;

    private final JButton playButton;

    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    private List<Release> releases = new ArrayList<>();


    public Launcher() {

        frame = new JFrame("TerraImmerse Launcher");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        frame.setResizable(false);

        JPanel root = new JPanel();
        root.setBackground(new Color(10, 12, 14));

        root.setBorder(new EmptyBorder(
                35, 45, 35, 45
        ));

        root.setLayout(new BorderLayout(20, 20));

        /*
         * =========================
         * Logo
         * =========================
         */

        JLabel logo = new JLabel();

        try {

            ImageIcon icon = new ImageIcon("logo.png");

            Image image = icon.getImage()
                    .getScaledInstance(
                            100,
                            100,
                            Image.SCALE_SMOOTH
                    );

            logo.setIcon(new ImageIcon(image));

        } catch (Exception ignored) {
        }

        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(
                logo,
                BorderLayout.NORTH
        );


        /*
         * =========================
         * Center
         * =========================
         */

        JPanel center = new JPanel();

        center.setOpaque(false);

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title = new JLabel(
                "TerraImmerse"
        );

        title.setForeground(
                new Color(240, 240, 240)
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        42
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitle = new JLabel(
                "A voxel world beyond imagination."
        );

        subtitle.setForeground(
                new Color(145, 155, 145)
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        17
                )
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        center.add(title);

        center.add(
                Box.createVerticalStrut(5)
        );

        center.add(subtitle);

        center.add(
                Box.createVerticalStrut(35)
        );


        /*
         * =========================
         * Version
         * =========================
         */

        JLabel versionLabel = new JLabel(
                "Version"
        );

        versionLabel.setForeground(
                Color.LIGHT_GRAY
        );

        versionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        versionBox = new JComboBox<>();

        versionBox.setMaximumSize(
                new Dimension(
                        400,
                        40
                )
        );

        versionBox.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        center.add(versionLabel);

        center.add(
                Box.createVerticalStrut(8)
        );

        center.add(versionBox);

        center.add(
                Box.createVerticalStrut(25)
        );


        /*
         * =========================
         * Edition
         * =========================
         */

        JLabel editionLabel = new JLabel(
                "Edition"
        );

        editionLabel.setForeground(
                Color.LIGHT_GRAY
        );

        editionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        fullEdition = new JRadioButton(
                "Full Edition"
        );

        piEdition = new JRadioButton(
                "Raspberry Pi Edition"
        );

        fullEdition.setOpaque(false);
        piEdition.setOpaque(false);

        fullEdition.setForeground(
                Color.LIGHT_GRAY
        );

        piEdition.setForeground(
                Color.LIGHT_GRAY
        );


        ButtonGroup group = new ButtonGroup();

        group.add(fullEdition);
        group.add(piEdition);

        fullEdition.setSelected(true);


        JPanel editionPanel = new JPanel();

        editionPanel.setOpaque(false);

        editionPanel.add(fullEdition);
        editionPanel.add(piEdition);

        editionPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        center.add(editionLabel);

        center.add(
                Box.createVerticalStrut(5)
        );

        center.add(editionPanel);

        center.add(
                Box.createVerticalStrut(25)
        );


        /*
         * =========================
         * Play Button
         * =========================
         */

        playButton = new JButton(
                "PLAY"
        );

        playButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        playButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        playButton.setPreferredSize(
                new Dimension(
                        220,
                        50
                )
        );

        playButton.addActionListener(
                e -> launchGame()
        );


        center.add(playButton);


        root.add(
                center,
                BorderLayout.CENTER
        );


        /*
         * =========================
         * Bottom
         * =========================
         */

        JPanel bottom = new JPanel();

        bottom.setOpaque(false);

        bottom.setLayout(
                new BorderLayout(5, 5)
        );


        statusLabel = new JLabel(
                "Loading releases..."
        );

        statusLabel.setForeground(
                new Color(140, 150, 140)
        );


        progressBar = new JProgressBar();

        progressBar.setStringPainted(true);

        progressBar.setVisible(false);


        bottom.add(
                statusLabel,
                BorderLayout.NORTH
        );

        bottom.add(
                progressBar,
                BorderLayout.SOUTH
        );


        root.add(
                bottom,
                BorderLayout.SOUTH
        );


        frame.setContentPane(root);

        frame.setVisible(true);


        loadReleases();
    }


    /*
     * =========================
     * Load releases.json
     * =========================
     */

    private void loadReleases() {

        new Thread(() -> {

            try {

                HttpClient client =
                        HttpClient.newHttpClient();

                HttpRequest request =
                        HttpRequest.newBuilder(
                                URI.create(
                                        RELEASES_URL
                                )
                        ).GET().build();


                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );


                if (response.statusCode() != 200) {

                    throw new IOException(
                            "HTTP " +
                            response.statusCode()
                    );
                }


                releases =
                        parseReleases(
                                response.body()
                        );


                SwingUtilities.invokeLater(() -> {

                    versionBox.removeAllItems();

                    for (Release release : releases) {

                        versionBox.addItem(
                                release
                        );
                    }


                    if (!releases.isEmpty()) {

                        statusLabel.setText(
                                "Ready"
                        );

                        playButton.setEnabled(
                                true
                        );

                    } else {

                        statusLabel.setText(
                                "No releases found."
                        );
                    }

                });


            } catch (Exception e) {

                SwingUtilities.invokeLater(() -> {

                    statusLabel.setText(
                            "Failed to load releases."
                    );

                    JOptionPane.showMessageDialog(
                            frame,
                            e.getMessage(),
                            "TerraImmerse Launcher",
                            JOptionPane.ERROR_MESSAGE
                    );

                });

            }

        }).start();
    }


    /*
     * =========================
     * JSON parser
     * =========================
     */

    private List<Release> parseReleases(
            String json
    ) {

        List<Release> result =
                new ArrayList<>();


        Pattern objectPattern =
                Pattern.compile(
                        "\\{(.*?)\\}",
                        Pattern.DOTALL
                );


        Matcher matcher =
                objectPattern.matcher(json);


        while (matcher.find()) {

            String object =
                    matcher.group(1);


            String phase =
                    getJsonValue(
                            object,
                            "phase"
                    );

            String version =
                    getJsonValue(
                            object,
                            "version"
                    );

            String displayName =
                    getJsonValue(
                            object,
                            "display_name"
                    );

            String full =
                    getJsonValue(
                            object,
                            "full_edition"
                    );

            String pi =
                    getJsonValue(
                            object,
                            "pi_edition"
                    );


            if (
                    version != null &&
                    displayName != null &&
                    full != null &&
                    pi != null
            ) {

                result.add(
                        new Release(
                                phase,
                                version,
                                displayName,
                                full,
                                pi
                        )
                );
            }
        }


        return result;
    }


    private String getJsonValue(
            String json,
            String key
    ) {

        Pattern pattern =
                Pattern.compile(
                        "\"" +
                        Pattern.quote(key) +
                        "\"\\s*:\\s*\"([^\"]*)\""
                );


        Matcher matcher =
                pattern.matcher(json);


        if (matcher.find()) {

            return matcher.group(1);
        }


        return null;
    }


    /*
     * =========================
     * Launch
     * =========================
     */

    private void launchGame() {

        Release release =
                (Release) versionBox.getSelectedItem();


        if (release == null) {

            return;
        }


        boolean pi =
                piEdition.isSelected();


        String jarPath =
                pi
                        ? release.piEdition
                        : release.fullEdition;


        new Thread(() -> {

            try {

                playButton.setEnabled(false);

                setStatus(
                        "Preparing TerraImmerse..."
                );


                Path versionDirectory =
                        VERSIONS_DIRECTORY
                                .resolve(
                                        release.phase
                                )
                                .resolve(
                                        release.version
                                );


                Files.createDirectories(
                        versionDirectory
                );


                /*
                 * =========================
                 * Download JAR
                 * =========================
                 */

                String jarName =
                        Paths.get(jarPath)
                                .getFileName()
                                .toString();


                Path jar =
                        versionDirectory
                                .resolve(jarName);


                String jarUrl =
                        BASE_URL + jarPath;


                download(
                        jarUrl,
                        jar
                );


                /*
                 * =========================
                 * Download natives
                 * =========================
                 */

                String platform =
                        detectPlatform();


                Path nativeDirectory =
                        NATIVES_DIRECTORY
                                .resolve(platform);


                Files.createDirectories(
                        nativeDirectory
                );


                String[] nativeFiles =
                        getNativeFiles(
                                platform
                        );


                for (String nativeFile :
                        nativeFiles) {

                    Path target =
                            nativeDirectory
                                    .resolve(nativeFile);


                    String url =
                            BASE_URL +
                            "libs/lwjgl/natives/" +
                            platform +
                            "/natives/" +
                            nativeFile;


                    download(
                            url,
                            target
                    );
                }


                /*
                 * =========================
                 * Start game
                 * =========================
                 */

                setStatus(
                        "Starting TerraImmerse..."
                );


                startGame(
                        jar,
                        nativeDirectory
                );


                SwingUtilities.invokeLater(
                        frame::dispose
                );


            } catch (Exception e) {

                SwingUtilities.invokeLater(() -> {

                    playButton.setEnabled(true);

                    statusLabel.setText(
                            "Launch failed."
                    );

                    JOptionPane.showMessageDialog(
                            frame,
                            e.getMessage(),
                            "TerraImmerse",
                            JOptionPane.ERROR_MESSAGE
                    );

                });

            }

        }).start();
    }


    /*
     * =========================
     * Download
     * =========================
     */

    private void download(
            String url,
            Path target
    ) throws Exception {

        setStatus(
                "Downloading " +
                target.getFileName()
        );


        HttpClient client =
                HttpClient.newHttpClient();


        HttpRequest request =
                HttpRequest.newBuilder(
                        URI.create(url)
                ).GET().build();


        HttpResponse<InputStream> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofInputStream()
                );


        if (response.statusCode() != 200) {

            throw new IOException(
                    "Download failed: HTTP " +
                    response.statusCode() +
                    "\n" +
                    url
            );
        }


        Files.createDirectories(
                target.getParent()
        );


        try (
                InputStream input =
                        response.body();

                OutputStream output =
                        Files.newOutputStream(
                                target,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING
                        )
        ) {

            byte[] buffer =
                    new byte[8192];

            int read;

            while (
                    (read = input.read(buffer))
                    != -1
            ) {

                output.write(
                        buffer,
                        0,
                        read
                );
            }
        }
    }


    /*
     * =========================
     * Platform detection
     * =========================
     */

    private String detectPlatform() {

        String os =
                System.getProperty(
                        "os.name"
                ).toLowerCase();

        String arch =
                System.getProperty(
                        "os.arch"
                ).toLowerCase();


        if (os.contains("win")) {

            if (
                    arch.contains("aarch64") ||
                    arch.contains("arm64")
            ) {

                return "windows-arm64";
            }

            if (
                    arch.contains("x86") &&
                    !arch.contains("64")
            ) {

                return "windows-x86";
            }

            return "windows-x64";
        }


        if (os.contains("mac")) {

            if (
                    arch.contains("aarch64") ||
                    arch.contains("arm64")
            ) {

                return "macos-arm64";
            }

            return "macos-x64";
        }


        if (os.contains("linux")) {

            if (
                    arch.equals("arm") ||
                    arch.equals("arm32") ||
                    arch.equals("armv7l")
            ) {

                return "linux-arm32";
            }

            if (
                    arch.contains("aarch64") ||
                    arch.contains("arm64")
            ) {

                return "linux-arm64";
            }

            if (arch.contains("ppc64")) {

                return "linux-ppc64le";
            }

            if (arch.contains("riscv64")) {

                return "linux-riscv64";
            }

            return "linux-x64";
        }


        throw new RuntimeException(
                "Unsupported operating system: " +
                os +
                " / " +
                arch
        );
    }


    /*
     * =========================
     * Native files
     * =========================
     */

    private String[] getNativeFiles(
            String platform
    ) {

        if (
                platform.startsWith(
                        "windows"
                )
        ) {

            return new String[] {
                    "glfw.dll",
                    "lwjgl.dll",
                    "lwjgl_opengl.dll",
                    "lwjgl_stb.dll"
            };
        }


        if (
                platform.startsWith(
                        "macos"
                )
        ) {

            return new String[] {
                    "libglfw.dylib",
                    "liblwjgl.dylib",
                    "liblwjgl_opengl.dylib",
                    "liblwjgl_stb.dylib"
            };
        }


        return new String[] {
                "libglfw.so",
                "liblwjgl.so",
                "liblwjgl_opengl.so",
                "liblwjgl_stb.so"
        };
    }


    /*
     * =========================
     * Start game
     * =========================
     */

    private void startGame(
            Path jar,
            Path natives
    ) throws Exception {

        List<String> command =
                new ArrayList<>();


        command.add(
                findJava()
        );


        command.add(
                "-Djava.library.path=" +
                natives.toAbsolutePath()
        );


        command.add(
                "-jar"
        );


        command.add(
                jar.toAbsolutePath().toString()
        );


        ProcessBuilder builder =
                new ProcessBuilder(
                        command
                );


        builder.directory(
                GAME_DIRECTORY.toFile()
        );


        builder.inheritIO();

        builder.start();
    }


    private String findJava() {

        String javaHome =
                System.getProperty(
                        "java.home"
                );


        Path java =
                Paths.get(
                        javaHome,
                        "bin",
                        isWindows()
                                ? "java.exe"
                                : "java"
                );


        return java.toString();
    }


    private boolean isWindows() {

        return System
                .getProperty(
                        "os.name"
                )
                .toLowerCase()
                .contains("win");
    }


    /*
     * =========================
     * Status
     * =========================
     */

    private void setStatus(
            String text
    ) {

        SwingUtilities.invokeLater(
                () ->
                        statusLabel.setText(text)
        );
    }


    /*
     * =========================
     * Main
     * =========================
     */

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                Launcher::new
        );
    }


    /*
     * =========================
     * Release
     * =========================
     */

    private static class Release {

        final String phase;
        final String version;
        final String displayName;
        final String fullEdition;
        final String piEdition;


        Release(
                String phase,
                String version,
                String displayName,
                String fullEdition,
                String piEdition
        ) {

            this.phase =
                    phase;

            this.version =
                    version;

            this.displayName =
                    displayName;

            this.fullEdition =
                    fullEdition;

            this.piEdition =
                    piEdition;
        }


        @Override
        public String toString() {

            return displayName;
        }
    }
}
