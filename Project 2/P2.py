import numpy as np
import csv
import matplotlib.pyplot as plt


def main():
    data = openfile("irisdata.csv")
    vir_length = getdata(data, 2, "virginica")
    vir_width = getdata(data, 3, "virginica")
    ver_length = getdata(data, 2, "versicolor")
    ver_width = getdata(data, 3, "versicolor")
    set_length = getdata(data, 2, "setosa")
    set_width = getdata(data, 3, "setosa")

    # 1a
    plt.figure(1)
    plt.subplot(221)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.plot(vir_length, vir_width, "ro", label="Virgnica")
    plt.plot(ver_length, ver_width, "bo", label="Versicolor")
    plt.title("Petal Length vs Petal Width")
    plt.legend()

    # 1b
    plt.figure(1)
    plt.subplot(222)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.plot(vir_length, vir_width, "ro", label="Virgnica")
    plt.plot(ver_length, ver_width, "bo", label="Versicolor")
    x = np.arange(2.5, 7)
    y = -2.5 / 6 * x + 3.7
    plt.plot(x, y)
    plt.title("Linear Decision Boundary")
    plt.legend()

    # 1c
    virginica = splitdata(data, "virginica")
    versicolor = splitdata(data, "versicolor")
    plt.figure(1)
    plt.subplot(223)
    classify(virginica, versicolor, -2.5 / 6, 3.7)

    # 1d
    plt.figure(2)
    plt.subplot(121)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.title("Circle Decision Boundary")
    plt.plot(vir_length, vir_width, "ro", label="Virgnica")
    plt.plot(ver_length, ver_width, "bo", label="Versicolor")
    plt.plot(set_length, set_width, "go", label="Setosa")
    circle = plt.Circle((5.9, 2.4), radius=1.3, fc='r', alpha=0.25)
    plt.gca().add_patch(circle)
    circle = plt.Circle((3.75, .93), radius=1.32, fc='b', alpha=0.25)
    plt.gca().add_patch(circle)
    circle = plt.Circle((1.5, .25), radius=1, fc='g', alpha=0.25)
    plt.gca().add_patch(circle)
    plt.legend()
    setosa = splitdata(data, "setosa")
    circleclassify(virginica, versicolor, setosa)

    # 2a and 2b
    w = np.array([.28, 1.5])
    b = -3.9
    smallerror = mse(data, (w, b), ("virginica", "versicolor"))
    print("Small mse is", smallerror)
    plt.figure(3)
    plt.subplot(121)
    classify(virginica, versicolor, (-w[0] / w[1]), -b / w[1])
    plt.title("Small MSE")
    w = np.array([10, 10])
    b = .5
    largeerror = mse(data, (w, .5), ("virginica", "versicolor"))
    print("Large mse is ", largeerror)
    virginica = splitdata(data, "virginica")
    versicolor = splitdata(data, "versicolor")
    plt.subplot(122)
    classify(virginica, versicolor, (-w[0] / w[1]), -b / w[1])
    plt.title("Large MSE")

    # 2e
    plt.figure(4)
    n = 0
    w = np.array([0.22, 1.3])
    b = -2.5
    x = np.arange(2.5, 7)
    y = (-w[0] / w[1]) * x - (b / w[1])
    plt.plot(x, y, label="Iteration %d" % n)
    dw, db = gradient(data, (w, b), ('virginica', 'versicolor'))
    x = np.arange(2.5, 7)
    w -= 0.01 * dw
    b -= 0.01 * db
    y = (-w[0] / w[1]) * x - (b / w[1])
    n = n + 1
    plt.plot(x, y, label="Iteration %d" % n)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.plot(vir_length, vir_width, "ro", label="Virgnica")
    plt.plot(ver_length, ver_width, "bo", label="Versicolor")
    plt.legend()
    plt.title("Gradient Single Step")

    #3a-c
    plt.figure(5)
    # random starting weights
    np.random.seed(1)
    w = np.random.random(2)
    b = np.random.random()
    mses = []
    mses.append(mse(data, (w, b), ("virginica", "versicolor")))
    n = 0
    plt.subplot(121)
    y = (-w[0] / w[1]) * x - (b / w[1])
    n = n + 1
    plt.plot(x, y, label="Iteration %d" % n)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.title("Gradient")
    plt.plot(vir_length, vir_width, "ro", label="Virgnica")
    plt.plot(ver_length, ver_width, "bo", label="Versicolor")
    plt.legend()
    plt.subplot(122)
    plt.xlabel("Number of Iterations")
    plt.ylabel("Error")
    plt.ylabel("Beginning MSE vs Iterations")
    plt.plot(mses, "bo")
    while len(mses) < 2 or mses[-2] - mses[-1] > 1e-6:
        dw, db = gradient(data, (w, b), ('virginica', 'versicolor'))
        x = np.arange(2.5, 7)
        w -= 0.01 * dw
        b -= 0.01 * db
        mses.append(mse(data, (w, b), ("virginica", "versicolor")))
        n = n + 1
        if n == 5000:
            plt.figure(6)
            plt.subplot(121)
            y = (-w[0] / w[1]) * x - (b / w[1])
            n = n + 1
            plt.plot(x, y, label="Iteration %d" % n)
            plt.xlabel("Petal Length")
            plt.ylabel("Petal Width")
            plt.title("Gradient")
            plt.plot(vir_length, vir_width, "ro", label="Virgnica")
            plt.plot(ver_length, ver_width, "bo", label="Versicolor")
            plt.legend()
            plt.subplot(122)
            plt.xlabel("Number of Iterations")
            plt.ylabel("Middle MSE vs Iterations")
            plt.title("MSE")
            plt.plot(mses)
    plt.figure(7)
    plt.subplot(121)
    y = (-w[0] / w[1]) * x - (b / w[1])
    n = n + 1
    plt.plot(x, y, label="Iteration %d" % n)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.ylabel("End MSE vs Iterations")
    plt.plot(vir_length, vir_width, "ro", label="Virgnica")
    plt.plot(ver_length, ver_width, "bo", label="Versicolor")
    plt.legend()
    plt.subplot(122)
    plt.xlabel("Number of Iterations")
    plt.ylabel("Error")
    plt.title("MSE")
    plt.plot(mses)

    plt.show()

def gradient(data, boundary, pattern):
    virginica = np.array(splitdata(data, pattern[0]))[:, 2:4].astype(np.float32)
    versicolor = np.array(splitdata(data, pattern[1]))[:, 2:4].astype(np.float32)
    total = np.append(virginica, versicolor, 0)
    labels = np.append(np.ones(50), -np.ones(50))
    w = boundary[0]
    b = boundary[1]
    errors = np.dot(total, w) + b - labels
    gradientw = .01 * np.dot(total.T, errors)
    gradientb = .01 * errors.sum()
    return gradientw, gradientb

def mse(data, boundary, pattern):
    virginica = np.array(splitdata(data, pattern[0]))[:, 2:4].astype(np.float32)
    versicolor = np.array(splitdata(data, pattern[1]))[:, 2:4].astype(np.float32)
    total = np.append(virginica, versicolor, 0)
    labels = np.append(np.ones(50), -np.ones(50))
    w = boundary[0]
    b = boundary[1]
    errors = np.dot(total, w) + b - labels
    return (errors ** 2).mean()


def circleclassify(virginica, versicolor, setosa):
    newvirx = []
    newviry = []
    newverx = []
    newvery = []
    newsetx = []
    newsety = []
    misclassifiedx = []
    misclassifiedy = []
    for row1 in virginica:
        if (float(row1[2]) - 5.9) ** 2 + (float(row1[3]) - 2.4) ** 2 < 1.3 ** 2:
            newvirx.append(row1[2])
            newviry.append(row1[3])
        else:
            misclassifiedx.append(row1[2])
            misclassifiedy.append(row1[3])
    for row2 in versicolor:
        if (float(row2[2]) - 3.75) ** 2 + (float(row2[3]) - .93) ** 2 < 1.32 ** 2:
            newverx.append(row2[2])
            newvery.append(row2[3])
        else:
            misclassifiedx.append(row2[2])
            misclassifiedy.append(row2[3])
    for row3 in setosa:
        if (float(row3[2]) - 1.5) ** 2 + (float(row3[3]) - .25) ** 2 < 1:
            newsetx.append(row3[2])
            newsety.append(row3[3])
        else:
            misclassifiedx.append(row3[2])
            misclassifiedy.append(row3[3])
    plt.figure(2)
    plt.subplot(122)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.plot(newvirx, newviry, "ro", label="Virginica")
    plt.plot(newverx, newvery, "bo", label="Versicolor")
    plt.plot(newsetx, newsety, "go", label="Setosa")
    plt.plot(misclassifiedx, misclassifiedy, "kx", label="Misclassified")
    circle = plt.Circle((5.9, 2.4), radius=1.3, fc='r', alpha=0.25)
    plt.gca().add_patch(circle)
    circle = plt.Circle((3.75, .93), radius=1.32, fc='b', alpha=0.25)
    plt.gca().add_patch(circle)
    circle = plt.Circle((1.5, .25), radius=1, fc='g', alpha=0.25)
    plt.gca().add_patch(circle)
    plt.title("Classifying Points Based on Boundary")
    plt.legend()


def openfile(file):
    X = []
    f = open("irisdata.csv")
    for row in csv.reader(f):
        X.append(row)
    legend = X.pop(0)
    return X


def splitdata(data, type):
    split_data = []
    for row in data:
        if row[4] == type:
            split_data.append(row)
    return split_data


def getdata(data, column, type):
    flower_data = []
    for row in data:
        if row[4] == type:
            flower_data.append(row[column])
    return flower_data


def classify(virginica, versicolor, m, b):
    newvirx = []
    newviry = []
    newverx = []
    newvery = []
    for row1 in virginica:
        if float(row1[3]) > ((m * float(row1[2])) + b):
            newvirx.append(row1[2])
            newviry.append(row1[3])
        else:
            newverx.append(row1[2])
            newvery.append(row1[3])
    for row2 in versicolor:
        if float(row2[3]) > ((m * float(row2[2])) + b):
            newvirx.append(row2[2])
            newviry.append(row2[3])
        else:
            newverx.append(row2[2])
            newvery.append(row2[3])
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.plot(newvirx, newviry, "ro", label="Virginica")
    plt.plot(newverx, newvery, "bo", label="Versicolor")
    plt.title("Classifying Points Based on Boundary")
    x = np.arange(2.5, 7)
    y = m * x + b
    plt.plot(x, y)
    plt.legend()


if __name__ == "__main__":
    main()
