package alex.tdi.utils;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.util.Arrays;

public class OkHttpHelper {
	// SET SSL
	public static OkHttpClient getSSLClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} };

			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
				throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
			}

			X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).build();
			return client;

		} catch (Exception e) {
			System.out.println("getSSLClient Exception:" + e);
			throw new RuntimeException(e);
		}
	}
}
