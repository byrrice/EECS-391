import matplotlib.pyplot as plotter
import operator as op
import functools
import numpy as np
import random

def main():
    oneb()
    onec()
    twoa()
    twoc()
    plotter.show()


def oneb():
    n = 4
    theta = .75
    yrange = 5
    for y in range(yrange):
        prob = (ncr(n, y)) * (theta**y) * ((1-theta)**(n-y))
        plotter.figure(0)
        plotter.plot(y, prob, 'ro')
        plotter.title("Probability of y given n=4, theta=.75")
        plotter.xlabel("y")
        plotter.ylabel("Probability of y")

def onec():
    y = 1
    n = 1
    theta = np.arange(0, 1, .01)
    prob_array = []
    for x in theta:
        prob = (ncr(n, y)) * (theta**y) * ((1-theta)**(n-y)) * (n + 1)
        plotter.figure(1)
        plotter.plot(theta, prob, 'ro')
        plotter.plot(theta, prob, 'b')
        plotter.title("Probability of theta given y=1, n=1")
        plotter.xlabel("Theta")
        plotter.ylabel("Probability of Theta")
    y = 2
    n = 2
    for x in theta:
        prob = (ncr(n, y)) * (theta**y) * ((1-theta)**(n-y)) * (n + 1)
        plotter.figure(2)
        plotter.plot(theta, prob, 'ro')
        plotter.plot(theta, prob, 'b')
        plotter.title("Probability of theta given y=2, n=2")
        plotter.xlabel("Theta")
        plotter.ylabel("Probability of Theta")
    y = 2
    n = 3
    for x in theta:
        prob = (ncr(n, y)) * (theta**y) * ((1-theta)**(n-y)) * (n + 1)
        plotter.figure(3)
        plotter.plot(theta, prob, 'ro')
        plotter.plot(theta, prob, 'b')
        plotter.title("Probability of theta given y=2, n=3")
        plotter.xlabel("Theta")
        plotter.ylabel("Probability of Theta")
    y = 3
    n = 4
    for x in theta:
        prob = (ncr(n, y)) * (theta**y) * ((1-theta)**(n-y)) * (n + 1)
        plotter.figure(4)
        plotter.plot(theta, prob, 'ro')
        plotter.plot(theta, prob, 'b')
        plotter.title("Probability of theta given y=3, n=4")
        plotter.xlabel("Theta")
        plotter.ylabel("Probability of Theta")

def twoa():
    probability = [0, .25, .5, .75, 1]
    random_data = []
    for x in range(4):
        random_data.append(getData(probability[x]))

    for x in range(4):
        # here we cal the graph for each bag, and print out each value
        prior_data, lime_data = calcGraph(random_data[x])
        graphData(x, prior_data, lime_data)

def twoc():
    for x in range(4):
        ErrorReduction(x)

# generates the random data
def getData(probability):
    data = []
    #generate 100 values
    for x in range(100):
        if probability > 0:
            value = (random.uniform(0, 1) * 4)
        else:
            value = 1

        # 0 = lime, 1 = cherry
        if value < 4 * probability:
            data.append(0)
        else:
            data.append(1)

    return data

# calculates data needed for graph
def calcGraph(data):

    prior = [.1, .2, .4, .2, .1]
    probability = [0, .25, .5, .75, 1]
    prior_prob = [[.1], [.2], [.4], [.2], [.1]]

    # Starts off at .5, probability that you draw a lime
    lime_data = [.5]

    for x in range(len(data)):

        # probability of next candy
        candy_prob = 0

        # probability of drawing a lime
        lime_prob = 0


        for z in range(5):
            # updating the next candy probability
            if data[x] == 1:
                candy_prob = candy_prob + (prior[z] * (1 - probability[z]))
            else:
                candy_prob = candy_prob + (prior[z] * probability[z])

        # updating your probability of getting a lime
        if data[x] == 1:
            lime_prob = 1 - candy_prob
        else:
            lime_prob = candy_prob


        # updating the prior depending on whether you drew a cherry or lime
        for y in range(5):
            if data[x] == 1:
                prior[y] = (1 - probability[y]) * prior[y] / candy_prob
            else:
                prior[y] = probability[y] * prior[y] / candy_prob

            prior_prob[y].append(prior[y])

        lime_data.append(lime_prob)

    return prior_prob, lime_data

# plots all data
def graphData(bag, bag_prob, lime_data):

    prior = [.1, .2, .4, .2, .1]
    color = ['bo', 'ro', 'yo', 'go', 'ko']

    for x in range(5):
        plotter.figure(bag + 5)
        plotter.subplot(121)
        plotter.title('Bag h{}'.format(bag + 1))
        plotter.plot(0, prior[x], color[x])
        plotter.xlabel("Number of Observations in d")
        plotter.ylabel("Posterior Probability of Hypothesis")
        plotter.subplot(122)

    for y in range(5):
        plotter.figure(bag + 5)
        plotter.subplot(121)
        plotter.plot(range(101), bag_prob[y], color[y])

    plotter.subplot(122)
    plotter.plot(range(101), lime_data, 'ko')
    plotter.xlabel("Number of Observations in d")
    plotter.ylabel("Probability Next Candy is Lime")

# plots the error reduction
def ErrorReduction(bag):
    color = ['bo', 'ro', 'yo', 'go', 'ko']
    probability = [0, .25, .5, .75, 1]
    lime_error = []
    for x in range(50):
        prior_data, lime_data = calcGraph(getData(probability[bag]))
        lime_error.append(lime_data)

    average_data = []
    for x in range(100):
        data_sum = 0
        for y in range(50):
            data_sum += lime_error[y][x]
        average_data.append(data_sum/50)

    plotter.figure(bag + 9)
    plotter.plot(average_data, color[bag])
    plotter.title("Reduction in Uncertainty h{}".format(bag + 1))
    plotter.xlabel("Number of Candy")
    plotter.ylabel("Probability")


#ncr method from internet
def ncr(n, r):
    r = min(r, n-r)
    if r == 0: return 1
    numer = functools.reduce(op.mul, range(n, n-r, -1))
    denom = functools.reduce(op.mul, range(1, r+1))
    return numer//denom

if __name__ == "__main__":
    main()