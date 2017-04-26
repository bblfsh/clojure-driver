include .sdk/Makefile

test-native:
	cd native; \
	lein test

build-native:
	cd native; \
	lein uberjar; \
	mv ./target/uberjar/*-standalone.jar $(BUILD_PATH); \
	echo -e "#!/bin/bash\njava -jar $(find *-standalone.jar)" > $(BUILD_PATH)/native; \
	chmod +x $(BUILD_PATH)/native
