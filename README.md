# Bitcoin-Stealer
***Random bitcoin wallet generator, searching for wallet balance***<br>
This project requires java 17 and uses current libraries ([build.gradle](https://github.com/Delta203/Bitcoin-Stealer/blob/main/build.gradle)):
- [org.bitcoinj:bitcoinj-core:0.16.2](https://bitcoinj.org/)
- [com.google.code.gson:gson:2.8.9](https://code.google.com/)
___
### Download here: [Bitcoin-Stealer-final.jar](https://github.com/Delta203/Bitcoin-Stealer/raw/main/build/libs/Bitcoin-Stealer-final.jar)
***Execution:*** `java -jar Bitcoin-Stealer-final.jar <debug (default=100)>`
___
1. Create a private and public key pair based on elliptic curves
```
public static ECKey generateECKey() {}
```
2. Create the P2PKH address from public key with network byte 00
```
public static Address createAddressFromKey(ECKey key) {}
```
3. Get the wallet data from bitcoin scanner API. The API has to allow unlimited requests, because the scanner is going to check every address in a very short time. This project is currently using the API from: [https://btcscan.org/api/](https://btcscan.org/api/) 
```
public static String readWalletContent(Address address) throws IOException {}
```
4. Convert a json string into json dictionary
```
public static JsonObject convertStringToJson(String jsonString) {}
```
5. Check if the funded_amount - spend_amount > 0 ⇔ wallet amount > 0. <br> If bitcoins found, then print the private key and other useful data
```
System.out.println("Private Key: " + key.getPrivateKeyAsWiF(NetworkParameters.fromID(NetworkParameters.ID_MAINNET)));
System.out.println("Public Key: " + Utils.HEX.encode(publicKey));
System.out.println("Bitcoin Address: " + address.toString());
System.out.println(walletContent);
```
___
Buy me a :coffee::smile:? <br>
My bitcoin: 39dR2kBE6DVEK8mAGSvWX6HVDrEawP3THv