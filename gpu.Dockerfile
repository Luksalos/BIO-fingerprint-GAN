FROM tensorflow/tensorflow:1.8.0-gpu-py3

ENV PYTHONUNBUFFERED 1 # Do not buffer standart output from python
ENV DEBIAN_FRONTEND=noninteractive

# To fix OpenCV. Problem is that tensorflow:1.8.0-gpu-py3 is old image.
RUN apt-get update && \
    apt-get install -y libsm6 libxext6 libxrender-dev && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir matplotlib opencv-python Pillow tqdm

WORKDIR /tf