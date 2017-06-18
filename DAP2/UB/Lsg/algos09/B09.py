from copy import deepcopy

class Algorithm:

    def __init__(self, a, b):
        print(self.a9_1b(a, b))

    def a9_1b(self, a, b):
        self.a, self.b = a, b
        self.initK()
        for i in range(1, len(a)+1):
            for j in range(1, len(b)+1):
                self.evaluate(i, j)
        #return K[len(a)+1][len(b)+1]
        return self.K

    def initK(self):
        self.K = [[0]]
        for i in range(1, len(self.a)+1):
            if self.a[i-1] == 0:
                ref = self.K[i-1]
            else:
                ref = [self.K[i-1][0]+2]
            self.K.append(deepcopy(ref))

        for i in range(1, len(self.b)+1):
            if self.b[i-1] == 0:
                ref = self.K[0][i-1]
            else:
                ref = self.K[0][i-1]+2
            self.K[0].append(deepcopy(ref))

    def evaluate(self, i, j):
        indA = i-1
        indB = j-1
        # gleichheit oder einfuegen von 0
        if  (i == j and self.a[indA] == self.b[indB]) \
        or (j>i and  self.a[indA] == self.b[indB] and self.b[indB-1] == 0) \
        or (i>j and  self.a[indA] == self.b[indB] and self.a[indA-1] == 0):
            ref = self.K[i-1][j-1]
            self.K[i].append(deepcopy(ref))
        else:
            minimum = min(
                self.K[i-1][j-1], self.K[i-1][j], self.K[i][j-1]
            )
            # entfernen von 0
            if (self.a[indA] == 0 and i>j) \
            or (self.b[indB] == 0 and j>i):
                self.K[i].append(deepcopy(minimum))
            # ersetzen
            else:
                self.K[i].append(deepcopy(minimum + 2))

if __name__ == '__main__':
    result = Algorithm([1,1,1,0], [1,0,1])
