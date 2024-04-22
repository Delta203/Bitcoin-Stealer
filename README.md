# Bitcoin-Stealer
Random bitcoin wallet generator, searching for wallet balance.

## Libraries ([build.gradle](https://github.com/Delta203/Bitcoin-Stealer/blob/main/build.gradle))
- java 17 
- [org.bitcoinj:bitcoinj-core:0.16.2](https://bitcoinj.org/)
- [com.google.code.gson:gson:2.8.9](https://code.google.com/)

## Download
**Download:** [Bitcoin-Stealer-final.jar](https://github.com/Delta203/Bitcoin-Stealer/raw/main/build/libs/Bitcoin-Stealer-final.jar)<br>
**Execution:** `java -jar Bitcoin-Stealer-final.jar`

## Process
1. Create a random private and public key pair based on elliptic curves.
```java
public ECKey getRandomECKey() {}
```
2. Create the P2PKH wallet address from public key with network byte 00.
```java
public Address getAddressFromKey(ECKey key) {}
```
3. Get the wallet data from bitcoin scanner. The url api has to allow unlimited requests, because the scanner is going to check many addresses in a very short time. This project is currently using the API from: [https://btcscan.org/api/](https://btcscan.org/api/) 
```java
public String getWalletContent(String url, Address address) throws IOException {}
```
4. Convert a json string into json dictionary.
```java
public JsonObject stringToJson(String jsonString) {}
```
5. Check if `funded` - `spend` = 0 ⇔ wallet amount > 0 or wallet amount < 0.
```java
publicKey = Utils.HEX.encode(publicKey);
privateKey = key.getPrivateKeyAsWiF(NetworkParameters.fromID(NetworkParameters.ID_MAINNET));
address = address.toString();
```
