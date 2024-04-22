package de.bitcoinstealer.delta203;

import com.google.gson.JsonObject;
import de.bitcoinstealer.delta203.files.ConfigManager;
import de.bitcoinstealer.delta203.utils.WalletHandler;
import java.io.*;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;

public class Application extends WalletHandler {

  private String url;
  private int debug;

  public Application() {}

  public void run() {
    initConfig();
    mainLoop();
  }

  private void initConfig() {
    ConfigManager config = new ConfigManager();
    config.create();
    config.read();
    url = config.getUrl();
    debug = config.getDebug();
  }

  private void mainLoop() {
    int count = 0;
    while (true) {
      ECKey key = getRandomECKey();
      byte[] publicKey = key.getPubKey();
      Address address = getAddressFromKey(key);
      String content = null;
      while (content == null) {
        try {
          content = getWalletContent(url, address);
        } catch (IOException ignored) { // no response from api
        }
      }
      JsonObject wallet = stringToJson(content);
      // check wallet amount
      int funded = wallet.get("chain_stats").getAsJsonObject().get("funded_txo_sum").getAsInt();
      int spend = wallet.get("chain_stats").getAsJsonObject().get("spent_txo_sum").getAsInt();
      if (funded - spend != 0) { // -> wallet with bitcoins found
        bitcoinsFound(count, key, publicKey, address, wallet);
      }
      if (count % debug == 0) {
        System.out.println("Scanned " + count + " bitcoin addresses \t" + address);
      }
      count++;
    }
  }

  /**
   * Write address data into console and file.
   *
   * @param count the amount of addresses scanned
   * @param key the key pair
   * @param publicKey the public key
   * @param address the wallet address
   * @param wallet the walled json data
   */
  private void bitcoinsFound(
      int count, ECKey key, byte[] publicKey, Address address, JsonObject wallet) {
    String pubKey = Utils.HEX.encode(publicKey);
    String privateKey =
        key.getPrivateKeyAsWiF(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
    System.out.println("\nScan (" + count + ") BITCOIN WALLET FOUND!!!");
    System.out.println("Private Key: " + privateKey);
    System.out.println("Public Key: " + pubKey);
    System.out.println("Bitcoin Address: " + address.toString());
    System.out.println(wallet);
    // save key data
    try (PrintWriter writer = new PrintWriter(new FileOutputStream("found-wallets.txt", true))) {
      writer.println("Private Key: " + privateKey);
      writer.println("Public Key: " + pubKey);
      writer.println("Bitcoin Address: " + address);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
