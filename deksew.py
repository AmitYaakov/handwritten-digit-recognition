import sys
import numpy as np
import pandas as pd
from scipy.ndimage import interpolation
import os
from pathlib import Path


def tmp_dir():
    path = "/tmp/mnist_files/"
    try:
        os.mkdir(path)
    except OSError:
        print("failed to create %s" , path)

def moments(image):
    c0, c1 = np.mgrid[:image.shape[0], :image.shape[1]]
    totalImage = np.sum(image)
    m0 = np.sum(c0 * image) / totalImage
    m1 = np.sum(c1 * image) / totalImage
    m00 = np.sum((c0 - m0) ** 2 * image) / totalImage
    m11 = np.sum((c1 - m1) ** 2 * image) / totalImage
    m01 = np.sum((c0 - m0) * (c1 - m1) * image) / totalImage
    mu_vector = np.array([m0, m1])
    covariance_matrix = np.array([[m00, m01], [m01, m11]])
    return mu_vector, covariance_matrix

def deskew(image):
    c, v = moments(image)
    alpha = v[0, 1] / v[0, 0]
    affine = np.array([[1, 0], [alpha, 1]])
    ocenter = np.array(image.shape) / 2.0
    offset = c - np.dot(affine, ocenter)
    return interpolation.affine_transform(image, affine, offset=offset)


def create_new_csv(file_path):
    file_name = Path(file_path).name
    new_file_name = "desk_" + file_name;
    new_file_path =  "/tmp/mnist_files/" + new_file_name
    try:
        training_set = pd.read_csv(file_path, header=None)
        labels = training_set.iloc[:, :1]
        images = training_set.iloc[:, 1:]
        df_images = pd.DataFrame(images)
        df_images = df_images.apply(lambda x: pd.Series(
        deskew(x.values.reshape(28, 28)).flatten()), axis=1)

       # df_images = df_images.apply(lambda image: pd.Series(image))
        df_labels = pd.DataFrame(labels)
        new_csv = pd.concat([df_labels, df_images], axis=1)
        new_csv.columns = ["label"] + list(range(784))
        new_csv.to_csv(new_file_path,header=False, index=False)
    except:
        print("failed in reading/writing new file")
        return

def main(file_path):
    if (os.path.exists("/tmp/mnist_files/") == False):
      tmp_dir()
    create_new_csv(file_path)

if __name__ == "__main__":
    main(sys.argv[1])
