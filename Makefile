.PHONY: docker-run-gpu docker-build

all: docker-run-gpu

JUP_PORT:=8888
IMAGE_NAME:=bio-fingerprint/tensorflow:1.8.0-gpu-py3

# -p 6006-6015:6006-6015 for tensorboard
docker-run-gpu:
	@docker run --gpus all -it --rm \
		-p 6006-6015:6006-6015 \
		-v $(shell pwd):/tf/ \
		$(IMAGE_NAME) bash

docker-run-gpu-jupyter:
	@docker run --gpus all -it --rm \
		-p $(JUP_PORT):8888 \
		-p 6006-6015:6006-6015 \
		-v $(shell pwd):/tf/ \
		$(IMAGE_NAME)

docker-build:
	@docker build \
		-t $(IMAGE_NAME) \
		-f gpu.Dockerfile .
