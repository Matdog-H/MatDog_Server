#!/usr/bin/env python
# coding: utf-8

import dlib, cv2
import wget
import warnings
import os

detector = dlib.cnn_face_detection_model_v1('dogHeadDetector.dat')

warnings.filterwarnings("ignore", category=ResourceWarning)

def url_read(url):
    #강아지 이미지 로드
    file= wget.download(url)
    img = cv2.imread(file)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img = cv2.resize(img, dsize=None, fx=0.5, fy=0.5)

    os.close(file.fileno())

    #강아지 얼굴 인식
    dets = detector(img, upsample_num_times=1)

    # 반복문 사용해서 네모난 틀 만들기
    for i, d in enumerate(dets):
        x1, y1 = d.rect.left(), d.rect.top()
        x2, y2 = d.rect.right(), d.rect.bottom()
        s = 'Yes'
    return s


