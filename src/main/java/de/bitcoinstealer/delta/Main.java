package de.bitcoinstealer.delta;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.script.Script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

public class Main {

  public static String bitcoinScannerUrl = "https://btcscan.org/api/address/";
  public static int debugCount = 100;

  public static void main(String[] args) throws IOException {
    try {
      debugCount = Integer.decode(args[0]);
    } catch (Exception e) {
    }

    // run main loop with action counter
    int count = 0;
    while (true) {
      ECKey key = generateECKey();
      byte[] publicKey = key.getPubKey();

      Address address = createAddressFromKey(key);
      String walletContent = readWalletContent(address);
      JsonObject walletJson = convertStringToJson(walletContent);

      // check wallet amount
      int funded = walletJson.get("chain_stats").getAsJsonObject().get("funded_txo_sum").getAsInt();
      int spend = walletJson.get("chain_stats").getAsJsonObject().get("spent_txo_sum").getAsInt();
      if (funded - spend != 0) { // -> wallet with bitcoins found
        System.out.println(
            "Private Key: "
                + key.getPrivateKeyAsWiF(NetworkParameters.fromID(NetworkParameters.ID_MAINNET)));
        System.out.println("Public Key: " + Utils.HEX.encode(publicKey));
        System.out.println("Bitcoin Address: " + address.toString());
        System.out.println(walletContent);
        break;
      }
      if (count % debugCount == 0) {
        System.out.println("Scanned " + count + " bitcoin addresses \t" + address.toString());
      }
      count++;
    }
  }

  /**
   * <b>Bitcoin-Stealer</b><br>
   * Create a private and public key pair based on elliptic curves
   *
   * @return ECKey
   */
  public static ECKey generateECKey() {
    return new ECKey(new SecureRandom());
  }

  /**
   * <b>Bitcoin-Stealer</b><br>
   * Create the p2pkh address from public key with network byte 00
   *
   * @param key
   * @return Address
   */
  public static Address createAddressFromKey(ECKey key) {
    return Address.fromKey(
        Objects.requireNonNull(NetworkParameters.fromID(NetworkParameters.ID_MAINNET)),
        key,
        Script.ScriptType.P2PKH);
  }

  /**
   * <b>Bitcoin-Stealer</b><br>
   * Get the wallet data from bitcoin scanner api. The api has to allow unlimited requests, because
   * the scanner is going to check every address in a very short time.
   *
   * @param address
   * @return String
   * @throws IOException
   */
  public static String readWalletContent(Address address) throws IOException {
    String websiteURL = bitcoinScannerUrl + address.toString();
    URL url = new URL(websiteURL);
    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    String walletContent = reader.readLine();
    reader.close();
    return walletContent;
  }

  /**
   * <b>Bitcoin-Stealer</b><br>
   * Convert a json string into json dictionary
   *
   * @param jsonString
   * @return JsonObject
   */
  public static JsonObject convertStringToJson(String jsonString) {
    JsonParser jsonParser = new JsonParser();
    return JsonParser.parseString(jsonString).getAsJsonObject();
  }
}
