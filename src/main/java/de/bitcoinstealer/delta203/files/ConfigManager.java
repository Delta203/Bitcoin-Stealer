package de.bitcoinstealer.delta203.files;

import java.io.*;
import java.util.ArrayList;

public class ConfigManager {

  private final File file;
  private final ArrayList<String> content;

  public ConfigManager() {
    file = new File("config.txt");
    content = new ArrayList<>();
  }

  public String getUrl() {
    return content.get(0).split(": ")[1];
  }

  public int getDebug() {
    return Integer.decode(content.get(1).split(": ")[1]);
  }

  public void create() {
    if (file.exists()) return;
    try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
      writer.println("url: https://btcscan.org/api/address/");
      writer.println("debug: 100");
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void read() {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      content.add(reader.readLine());
      content.add(reader.readLine());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
