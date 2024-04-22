package de.bitcoinstealer.delta203.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Objects;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.Script;

public class WalletHandler {

  public WalletHandler() {}

  /**
   * Create a random private and public key pair based on elliptic curves.
   *
   * @return the random key pair
   */
  public ECKey getRandomECKey() {
    return new ECKey((new SecureRandom()));
  }

  /**
   * Create the P2PKH wallet address from public key with network byte 00.
   *
   * @param key the key pair
   * @return the wallet address
   */
  public Address getAddressFromKey(ECKey key) {
    return Address.fromKey(
        Objects.requireNonNull(NetworkParameters.fromID(NetworkParameters.ID_MAINNET)),
        key,
        Script.ScriptType.P2PKH);
  }

  /**
   * Get the wallet data from bitcoin scanner. The url api has to allow unlimited requests, because
   * the scanner is going to check many addresses in a very short time.
   *
   * @param url the scanner site
   * @param address the wallet address
   * @return the wallet data
   * @throws IOException
   */
  public String getWalletContent(String url, Address address) throws IOException {
    url += address.toString();
    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    String content = reader.readLine();
    reader.close();
    return content;
  }

  /**
   * Convert a json string into json dictionary.
   *
   * @param jsonString the json string
   * @return the json object
   */
  public JsonObject stringToJson(String jsonString) {
    return JsonParser.parseString(jsonString).getAsJsonObject();
  }
}
