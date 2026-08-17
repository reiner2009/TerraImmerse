package com.trhein.TerraImmerseLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Launcher {
   private static final String BASE_URL = "http://terraimmerse.t-rhein.com:8080/";
   private static final String RELEASES_URL = "http://terraimmerse.t-rhein.com:8080/resources/releases.json";
   private static final Path GAME_DIRECTORY = Paths.get(System.getProperty("user.home"), ".terraimmerse");
   private static final Path VERSIONS_DIRECTORY;
   private static final Path NATIVES_DIRECTORY;
   private final JFrame frame = new JFrame("TerraImmerse Launcher");
   private final JComboBox<Release> versionBox;
   private final JRadioButton fullEdition;
   private final JRadioButton piEdition;
   private final JButton playButton;
   private final JLabel statusLabel;
   private final JProgressBar progressBar;
   private List<Release> releases = new ArrayList<>();

   public Launcher() {
      this.frame.setDefaultCloseOperation(3);
      this.frame.setSize(700, 500);
      this.frame.setLocationRelativeTo((Component)null);
      this.frame.setResizable(false);
      JPanel root = new JPanel();
      root.setBackground(new Color(10, 12, 14));
      root.setBorder(new EmptyBorder(35, 45, 35, 45));
      root.setLayout(new BorderLayout(20, 20));
      JLabel logo = new JLabel();
      try {
         ImageIcon icon = new ImageIcon("logo.png");
         Image image = icon.getImage().getScaledInstance(100, 100, 4);
         logo.setIcon(new ImageIcon(image));
      } catch (Exception var11) {
      }
      logo.setHorizontalAlignment(0);
      root.add(logo, "North");
      JPanel center = new JPanel();
      center.setOpaque(false);
      center.setLayout(new BoxLayout(center, 1));
      JLabel title = new JLabel("TerraImmerse");
      title.setForeground(new Color(240, 240, 240));
      title.setFont(new Font("Arial", 1, 42));
      title.setAlignmentX(0.5F);
      JLabel subtitle = new JLabel("A voxel world beyond imagination.");
      subtitle.setForeground(new Color(145, 155, 145));
      subtitle.setFont(new Font("Arial", 0, 17));
      subtitle.setAlignmentX(0.5F);
      center.add(title);
      center.add(Box.createVerticalStrut(5));
      center.add(subtitle);
      center.add(Box.createVerticalStrut(35));
      JLabel versionLabel = new JLabel("Version");
      versionLabel.setForeground(Color.LIGHT_GRAY);
      versionLabel.setAlignmentX(0.5F);
      this.versionBox = new JComboBox<>();
      this.versionBox.setMaximumSize(new Dimension(400, 40));
      this.versionBox.setAlignmentX(0.5F);
      center.add(versionLabel);
      center.add(Box.createVerticalStrut(8));
      center.add(this.versionBox);
      center.add(Box.createVerticalStrut(25));
      JLabel editionLabel = new JLabel("Edition");
      editionLabel.setForeground(Color.LIGHT_GRAY);
      editionLabel.setAlignmentX(0.5F);
      this.fullEdition = new JRadioButton("Full Edition");
      this.piEdition = new JRadioButton("Raspberry Pi Edition");
      this.fullEdition.setOpaque(false);
      this.piEdition.setOpaque(false);
      this.fullEdition.setForeground(Color.LIGHT_GRAY);
      this.piEdition.setForeground(Color.LIGHT_GRAY);
      ButtonGroup group = new ButtonGroup();
      group.add(this.fullEdition);
      group.add(this.piEdition);
      this.fullEdition.setSelected(true);
      JPanel editionPanel = new JPanel();
      editionPanel.setOpaque(false);
      editionPanel.add(this.fullEdition);
      editionPanel.add(this.piEdition);
      editionPanel.setAlignmentX(0.5F);
      center.add(editionLabel);
      center.add(Box.createVerticalStrut(5));
      center.add(editionPanel);
      center.add(Box.createVerticalStrut(25));
      this.playButton = new JButton("PLAY");
      this.playButton.setFont(new Font("Arial", 1, 20));
      this.playButton.setAlignmentX(0.5F);
      this.playButton.setPreferredSize(new Dimension(220, 50));
      this.playButton.addActionListener((e) -> this.launchGame());
      center.add(this.playButton);
      root.add(center, "Center");
      JPanel bottom = new JPanel();
      bottom.setOpaque(false);
      bottom.setLayout(new BorderLayout(5, 5));
      this.statusLabel = new JLabel("Loading releases...");
      this.statusLabel.setForeground(new Color(140, 150, 140));
      this.progressBar = new JProgressBar();
      this.progressBar.setStringPainted(true);
      this.progressBar.setVisible(false);
      bottom.add(this.statusLabel, "North");
      bottom.add(this.progressBar, "South");
      root.add(bottom, "South");
      this.frame.setContentPane(root);
      this.frame.setVisible(true);
      this.loadReleases();
   }

   private void loadReleases() {
      (new Thread(() -> {
         try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(RELEASES_URL)).GET().build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
               throw new IOException("HTTP " + response.statusCode());
            }
            this.releases = this.parseReleases((String)response.body());
            SwingUtilities.invokeLater(() -> {
               this.versionBox.removeAllItems();
               for(Release release : this.releases) {
                  this.versionBox.addItem(release);
               }
               if (!this.releases.isEmpty()) {
                  this.statusLabel.setText("Ready");
                  this.playButton.setEnabled(true);
               } else {
                  this.statusLabel.setText("No releases found.");
               }
            });
         } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
               this.statusLabel.setText("Failed to load releases.");
               JOptionPane.showMessageDialog(this.frame, e.getMessage(), "TerraImmerse Launcher", 0);
            });
         }
      })).start();
   }

   private List<Release> parseReleases(String json) {
      List<Release> result = new ArrayList<>();
      Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", 32);
      Matcher matcher = objectPattern.matcher(json);
      while(matcher.find()) {
         String object = matcher.group(1);
         String phase = this.getJsonValue(object, "phase");
         String version = this.getJsonValue(object, "version");
         String displayName = this.getJsonValue(object, "display_name");
         String full = this.getJsonValue(object, "full_edition");
         String pi = this.getJsonValue(object, "pi_edition");
         String stableValue = this.getJsonBooleanValue(object, "stable");
         boolean stable = stableValue == null || Boolean.parseBoolean(stableValue);
         if (version != null && displayName != null && full != null && pi != null) {
            result.add(new Release(phase, version, displayName, full, pi, stable));
         }
      }
      return result;
   }

   private String getJsonValue(String json, String key) {
      Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
      Matcher matcher = pattern.matcher(json);
      return matcher.find() ? matcher.group(1) : null;
   }

   private String getJsonBooleanValue(String json, String key) {
      Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(true|false)");
      Matcher matcher = pattern.matcher(json);
      return matcher.find() ? matcher.group(1) : null;
   }

   private void launchGame() {
      Release release = (Release)this.versionBox.getSelectedItem();
      if (release != null) {
         if (!release.stable) {
            int choice = JOptionPane.showConfirmDialog(
               this.frame,
               "Warning: This is an unstable development/test release (" + release.displayName + ").\n" +
               "You may encounter bugs, crashes, or unexpected behavior.\n\n" +
               "Do you want to proceed anyway?",
               "Unstable Release Warning",
               JOptionPane.YES_NO_OPTION,
               JOptionPane.WARNING_MESSAGE
            );
            if (choice != JOptionPane.YES_OPTION) {
               return;
            }
         }
         boolean pi = this.piEdition.isSelected();
         String jarPath = pi ? release.piEdition : release.fullEdition;
         (new Thread(() -> {
            try {
               this.playButton.setEnabled(false);
               this.setStatus("Preparing TerraImmerse...");
               Path versionDirectory = VERSIONS_DIRECTORY.resolve(release.phase).resolve(release.version);
               Files.createDirectories(versionDirectory);
               String jarName = Paths.get(jarPath).getFileName().toString();
               Path jar = versionDirectory.resolve(jarName);
               String jarUrl = BASE_URL + jarPath;
               if (!Files.exists(jar)) {
                  this.download(jarUrl, jar);
               } else {
                  this.setStatus("Game executable ready.");
               }

               String platform = this.detectPlatform();
               Path nativeDirectory = NATIVES_DIRECTORY.resolve(platform);
               Files.createDirectories(nativeDirectory);
               String[] nativeFiles = this.getNativeFiles(platform);

               for(String nativeFile : nativeFiles) {
                  Path target = nativeDirectory.resolve(nativeFile);
                  String url = BASE_URL + "libs/lwjgl/natives/" + platform + "/" + nativeFile;
                  if (!Files.exists(target)) {
                     this.download(url, target);
                  }
               }

               this.setStatus("Starting TerraImmerse...");
               this.startGame(jar, nativeDirectory);
               JFrame var10000 = this.frame;
               Objects.requireNonNull(var10000);
               SwingUtilities.invokeLater(var10000::dispose);
            } catch (Exception e) {
               SwingUtilities.invokeLater(() -> {
                  this.playButton.setEnabled(true);
                  this.statusLabel.setText("Launch failed.");
                  JOptionPane.showMessageDialog(this.frame, e.getMessage(), "TerraImmerse", 0);
               });
            }
         })).start();
      }
   }

   private void download(String url, Path target) throws Exception {
      this.setStatus("Downloading " + String.valueOf(target.getFileName()));
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
      HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());
      if (response.statusCode() != 200) {
         int var10002 = response.statusCode();
         throw new IOException("Download failed: HTTP " + var10002 + "\n" + url);
      } else {
         Files.createDirectories(target.getParent());
         try (InputStream input = response.body();
              OutputStream output = Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            byte[] buffer = new byte[8192];
            int read;
            while((read = input.read(buffer)) != -1) {
               output.write(buffer, 0, read);
            }
         }
      }
   }

   private String detectPlatform() {
      String os = System.getProperty("os.name").toLowerCase();
      String arch = System.getProperty("os.arch").toLowerCase();
      if (os.contains("win")) {
         if (!arch.contains("aarch64") && !arch.contains("arm64")) {
            return arch.contains("x86") && !arch.contains("64") ? "windows-x86" : "windows-x64";
         } else {
            return "windows-arm64";
         }
      } else if (os.contains("mac")) {
         return !arch.contains("aarch64") && !arch.contains("arm64") ? "macos-x64" : "macos-arm64";
      } else if (os.contains("linux")) {
         if (!arch.equals("arm") && !arch.equals("arm32") && !arch.equals("armv7l")) {
            if (!arch.contains("aarch64") && !arch.contains("arm64")) {
               if (arch.contains("ppc64")) {
                  return "linux-ppc64le";
               } else {
                  return arch.contains("riscv64") ? "linux-riscv64" : "linux-x64";
               }
            } else {
               return "linux-arm64";
            }
         } else {
            return "linux-arm32";
         }
      } else {
         throw new RuntimeException("Unsupported operating system: " + os + " / " + arch);
      }
   }

   private String[] getNativeFiles(String platform) {
      if (platform.startsWith("windows")) {
         return new String[]{"glfw.dll", "lwjgl.dll", "lwjgl_opengl.dll", "lwjgl_stb.dll"};
      } else if (platform.startsWith("macos")) {
         return new String[]{"libglfw.dylib", "liblwjgl.dylib", "liblwjgl_opengl.dylib", "liblwjgl_stb.dylib"};
      } else {
         return new String[]{"libglfw.so", "liblwjgl.so", "liblwjgl_opengl.so", "liblwjgl_stb.so"};
      }
   }

   private void startGame(Path jar, Path natives) throws Exception {
      List<String> command = new ArrayList<>();
      command.add(this.findJava());

      command.add("--enable-native-access=ALL-UNNAMED");

      if (System.getProperty("os.name").toLowerCase().contains("mac")) {
         command.add("-XstartOnFirstThread");
      }

      String nativePath = natives.toAbsolutePath().toString();
      command.add("-Djava.library.path=" + nativePath);
      command.add("-Dorg.lwjgl.librarypath=" + nativePath);

      command.add("-jar");
      command.add(jar.toAbsolutePath().toString());

      ProcessBuilder builder = new ProcessBuilder(command);
      builder.directory(GAME_DIRECTORY.toFile());
      builder.inheritIO();
      builder.start();
   }

   private String findJava() {
      String javaHome = System.getProperty("java.home");
      Path java = Paths.get(javaHome, "bin", this.isWindows() ? "java.exe" : "java");
      return java.toString();
   }

   private boolean isWindows() {
      return System.getProperty("os.name").toLowerCase().contains("win");
   }

   private void setStatus(String text) {
      SwingUtilities.invokeLater(() -> this.statusLabel.setText(text));
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(Launcher::new);
   }

   static {
      VERSIONS_DIRECTORY = GAME_DIRECTORY.resolve("versions");
      NATIVES_DIRECTORY = GAME_DIRECTORY.resolve("natives");
   }

   private static class Release {
      final String phase;
      final String version;
      final String displayName;
      final String fullEdition;
      final String piEdition;
      final boolean stable;

      Release(String phase, String version, String displayName, String fullEdition, String piEdition, boolean stable) {
         this.phase = phase;
         this.version = version;
         this.displayName = displayName;
         this.fullEdition = fullEdition;
         this.piEdition = piEdition;
         this.stable = stable;
      }

      public String toString() {
         return this.displayName;
      }
   }
}