podman stop -t 0 sendgear
podman rm sendgear
podman run -d --name sendgear localhost/sendgear:2.0 /root/gateway_src/build/SendGear -1
