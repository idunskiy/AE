package com.library;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;


	 	//SSLContext sslContext = SSLContext.getInstance("BKS");
//	 	private static Context context;
//
//		protected static org.apache.http.conn.ssl.SSLSocketFactory createAdditionalCertsSSLSocketFactory() {
//	 	    try {
//	 	    	
//	 	    	
//	 	        final KeyStore ks = KeyStore.getInstance("BKS");
//
////	 	        // the bks file we generated above
////	 	        final InputStream in = context.getResources().openRawResource( R.raw);  
////	 	        try {
////	 	            // don't forget to put the password used above in strings.xml/mystore_password
////	 	            ks.load(in, context.getString( R.string.mystore_password ).toCharArray());
////	 	        } finally {
////	 	            in.close();
////	 	        }
//
//	 	        return new AdditionalKeyStoreSSLSocketFactory(ks);
//
//	 	    } catch( Exception e ) {
//	 	        throw new RuntimeException(e);
//	 	    }
//		}
//}
	 	
	     public class MySSLSocketFactory extends SSLSocketFactory {
	    	    SSLContext sslContext = SSLContext.getInstance("TLS");

	    	    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	    	        super(truststore);

	    	        TrustManager tm = new X509TrustManager() {
	    	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	    	            }

	    	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	    	            }

	    	            public X509Certificate[] getAcceptedIssuers() {
	    	                return null;
	    	            }
	    	        };

	    	        sslContext.init(null, new TrustManager[] { tm }, null);
	    	    }

	    	    @Override
	    	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	    	        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	    	    }

	    	    @Override
	    	    public Socket createSocket() throws IOException {
	    	        return sslContext.getSocketFactory().createSocket();
	    	    }
	    	}
	

