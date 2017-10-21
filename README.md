#generate a self-signed certificate and private key
openssl req -x509 -newkey rsa:4096 -keyout foo.key -out foo.cer -days 365

#put the private key and certificate in single pem for curl usage
cat foo.key foo.cer > foo.pem

#create the truststore (jks) with the foo.cer only
keytool -import -file foo.cer -alias Foo -keystore trust-foo-only.jks 

#convert the truststore from jks into pkcs12
keytool -importkeystore -srckeystore trust-foo-only.jks -srcstoretype JKS -deststoretype PKCS12 -destkeystore trust-foo-only.p12

#hit the endpoint via https (provided that password of private key  is passw0rd)
curl -k --cert src/main/resources/pki/foo.pem:passw0rd https://localhost:5443/conf

	
