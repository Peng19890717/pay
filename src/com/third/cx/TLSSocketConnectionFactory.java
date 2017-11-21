package com.third.cx;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;

import org.bouncycastle.crypto.tls.*;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Created by ka on 2016/11/10.
 */
public class TLSSocketConnectionFactory extends SSLSocketFactory {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    
    public Socket createSocket(Socket socket, final String host, int port,
                               boolean arg3) throws IOException {
        if (socket == null) {
            socket = new Socket();
        }
        if (!socket.isConnected()) {
            socket.connect(new InetSocketAddress(host, port));
        }

        final TlsClientProtocol tlsClientProtocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream(), new SecureRandom());

        return _createSSLSocket(host, tlsClientProtocol);
    }

    
    public String[] getDefaultCipherSuites() {
        return null;
    }

    
    public String[] getSupportedCipherSuites() {
        return null;
    }

    
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        throw new UnsupportedOperationException();
    }

    
    public Socket createSocket(InetAddress host, int port) throws IOException {
        throw new UnsupportedOperationException();
    }

    
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return null;
    }

    
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        throw new UnsupportedOperationException();
    }

    private SSLSocket _createSSLSocket(final String host, final TlsClientProtocol tlsClientProtocol) {
        return new SSLSocket() {
            private java.security.cert.Certificate[] peertCerts;

            
            public InputStream getInputStream() throws IOException {
                return tlsClientProtocol.getInputStream();
            }

            
            public OutputStream getOutputStream() throws IOException {
                return tlsClientProtocol.getOutputStream();
            }

            
            public synchronized void close() throws IOException {
                tlsClientProtocol.close();
            }

            
            public void addHandshakeCompletedListener(HandshakeCompletedListener arg0) {
            }

            
            public boolean getEnableSessionCreation() {
                return false;
            }

            
            public String[] getEnabledCipherSuites() {
                return null;
            }

            
            public String[] getEnabledProtocols() {
                return null;
            }

            
            public boolean getNeedClientAuth() {
                return false;
            }

            
            public SSLSession getSession() {
                return new SSLSession() {

                    
                    public int getApplicationBufferSize() {
                        return 0;
                    }

                    
                    public String getCipherSuite() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public long getCreationTime() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public byte[] getId() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public long getLastAccessedTime() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public java.security.cert.Certificate[] getLocalCertificates() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public Principal getLocalPrincipal() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public int getPacketBufferSize() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
                        return null;
                    }

                    
                    public java.security.cert.Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
                        return peertCerts;
                    }

                    
                    public String getPeerHost() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public int getPeerPort() {
                        return 0;
                    }

                    
                    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
                        return null;
                    }

                    
                    public String getProtocol() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public SSLSessionContext getSessionContext() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public Object getValue(String arg0) {
                        throw new UnsupportedOperationException();
                    }

                    
                    public String[] getValueNames() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public void invalidate() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public boolean isValid() {
                        throw new UnsupportedOperationException();
                    }

                    
                    public void putValue(String arg0, Object arg1) {
                        throw new UnsupportedOperationException();
                    }

                    
                    public void removeValue(String arg0) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            
            public String[] getSupportedProtocols() {
                return null;
            }

            
            public boolean getUseClientMode() {
                return false;
            }

            
            public boolean getWantClientAuth() {
                return false;
            }

            
            public void removeHandshakeCompletedListener(HandshakeCompletedListener arg0) {
            }

            
            public void setEnableSessionCreation(boolean arg0) {
            }

            
            public void setEnabledCipherSuites(String[] arg0) {
            }

            
            public void setEnabledProtocols(String[] arg0) {
            }

            
            public void setNeedClientAuth(boolean arg0) {
            }

            
            public void setUseClientMode(boolean arg0) {
            }

            
            public void setWantClientAuth(boolean arg0) {
            }

            
            public String[] getSupportedCipherSuites() {
                return null;
            }

            
            public void startHandshake() throws IOException {
                tlsClientProtocol.connect(new DefaultTlsClient() {

                    @SuppressWarnings("unchecked")
                    
                    public Hashtable<Integer, byte[]> getClientExtensions() throws IOException {
                        Hashtable<Integer, byte[]> clientExtensions = super.getClientExtensions();
                        if (clientExtensions == null) {
                            clientExtensions = new Hashtable<Integer, byte[]>();
                        }

                        //Add host_name
                        byte[] host_name = host.getBytes();

                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        final DataOutputStream dos = new DataOutputStream(baos);
                        dos.writeShort(host_name.length + 3);
                        dos.writeByte(0);
                        dos.writeShort(host_name.length);
                        dos.write(host_name);
                        dos.close();
                        clientExtensions.put(ExtensionType.server_name, baos.toByteArray());
                        return clientExtensions;
                    }

                    
                    public TlsAuthentication getAuthentication() throws IOException {
                        return new TlsAuthentication() {

                            
                            public void notifyServerCertificate(Certificate serverCertificate) throws IOException {
                                try {
                                    KeyStore ks = _loadKeyStore();

                                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                                    List<java.security.cert.Certificate> certs = new LinkedList<java.security.cert.Certificate>();
                                    boolean trustedCertificate = false;
                                    for (org.bouncycastle.asn1.x509.Certificate c : serverCertificate.getCertificateList()) {
                                        java.security.cert.Certificate cert = cf.generateCertificate(new ByteArrayInputStream(c.getEncoded()));
                                        certs.add(cert);

                                        String alias = ks.getCertificateAlias(cert);
                                        if (alias != null) {
                                            if (cert instanceof java.security.cert.X509Certificate) {
                                                try {
                                                    ((java.security.cert.X509Certificate) cert).checkValidity();
                                                    trustedCertificate = true;
                                                } catch (CertificateExpiredException cee) {
                                                    // Accept all the certs!
                                                }
                                            }
                                        } else {
                                            // Accept all the certs!
                                        }

                                    }
                                    if (!trustedCertificate) {
                                        // Accept all the certs!
                                    }
                                    peertCerts = certs.toArray(new java.security.cert.Certificate[0]);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    throw new IOException(ex);
                                }
                            }

                            
                            public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException {
                                return null;
                            }

                            private KeyStore _loadKeyStore() throws Exception {
                                FileInputStream trustStoreFis = null;
                                try {
                                    KeyStore localKeyStore = null;

                                    String trustStoreType = System.getProperty("javax.net.ssl.trustStoreType") != null ? System.getProperty("javax.net.ssl.trustStoreType") : KeyStore.getDefaultType();
                                    String trustStoreProvider = System.getProperty("javax.net.ssl.trustStoreProvider") != null ? System.getProperty("javax.net.ssl.trustStoreProvider") : "";

                                    if (trustStoreType.length() != 0) {
                                        if (trustStoreProvider.length() == 0) {
                                            localKeyStore = KeyStore.getInstance(trustStoreType);
                                        } else {
                                            localKeyStore = KeyStore.getInstance(trustStoreType, trustStoreProvider);
                                        }

                                        char[] keyStorePass = null;
                                        String str5 = System.getProperty("javax.net.ssl.trustStorePassword") != null ? System.getProperty("javax.net.ssl.trustStorePassword") : "";

                                        if (str5.length() != 0) {
                                            keyStorePass = str5.toCharArray();
                                        }

                                        localKeyStore.load(trustStoreFis, keyStorePass);

                                        if (keyStorePass != null) {
                                            for (int i = 0; i < keyStorePass.length; i++) {
                                                keyStorePass[i] = 0;
                                            }
                                        }
                                    }
                                    return localKeyStore;
                                } finally {
                                    if (trustStoreFis != null) {
                                        trustStoreFis.close();
                                    }
                                }
                            }
                        };
                    }

                });
            } // startHandshake
        };
    }
}