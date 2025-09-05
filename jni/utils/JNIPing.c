#include "jni.h"

#include <arpa/inet.h>

int get_local_ip(char *target);
int get_ttl_ip(char* target, int ttl);

jint Java_org_leeroy_filecorelibrary_samba_SambaDiscovery_findDoubleNatIp(JNIEnv * jenv) {
	return htonl(get_ttl_ip("8.8.8.8", 2));
}

jint Java_org_leeroy_filecorelibrary_samba_SambaDiscovery_findLocalIp(JNIEnv * jenv) {
	return htonl(get_local_ip("8.8.8.8"));
}
