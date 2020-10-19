#!/usr/bin/env python
# coding: utf-8

# In[29]:


from urllib import request
from io import BytesIO
import dlib, cv2, os, boto3, awscli
from imutils import face_utils
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as img
from PIL import Image
import urllib
import wget

detector = dlib.cnn_face_detection_model_v1('dogHeadDetector.dat')
predictor = dlib.shape_predictor('landmarkDetector.dat')

def url_read(url):
    #강아지 이미지 로드
    file= wget.download(url)
    img = cv2.imread(file)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img = cv2.resize(img, dsize=None, fx=0.5, fy=0.5)

    #강아지 얼굴 인식
    dets = detector(img, upsample_num_times=1)
    img_result = img.copy()

    # 반복문 사용해서 네모난 틀 만들기
    for i, d in enumerate(dets):
        x1, y1 = d.rect.left(), d.rect.top()
        x2, y2 = d.rect.right(), d.rect.bottom()
        cv2.rectangle(img_result, pt1=(x1, y1), pt2=(x2, y2), thickness=2, color=(255,0,0), lineType=cv2.LINE_AA)
        print('Yes')

