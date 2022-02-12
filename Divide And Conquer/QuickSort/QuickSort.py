import sys

def ChoosePivot(array, first, length):
	mid = -1
	last = length - 1 
	true_length = length - first
	if true_length % 2 == 0:
		mid = (true_length // 2) - 1    #length of the array is an even number
	else:
		mid = true_length // 2 				  #length of the array is an even number
	mid = first + mid
	piv_array = []
	piv_array.append(first)
	piv_array.append(last)
	piv_array.append(mid)
	max_int = -1
	max_pointer = -1
	min_int = 10001										#max int in the input file + 1
	min_pointer = -1
	for i in piv_array:
		if array[i] >= max_int:
			max_int = array[i]
			max_pointer = i
		if array[i] <= min_int:
			min_int = array[i]
			min_pointer = i
	pivot = -1
	for i in piv_array:
		if (i != max_pointer) and (i != min_pointer):
			pivot = i
	if pivot == -1:
		pivot = min_pointer
	return pivot

def swap(array, i, j):
	temp = array[i]
	array[i] = array[j]
	array[j] = temp

def partition(array, first, length):
	pivot = ChoosePivot(array, first, length)
	swap(array, pivot, first)
	pivot = first
	first_bigger_int = first + 1
	for next_unchecked_int in range(first_bigger_int, length):
		if array[next_unchecked_int] < array[pivot]:
			swap(array, first_bigger_int, next_unchecked_int)
			first_bigger_int += 1
	swap(array, pivot, first_bigger_int-1)
	pivot = first_bigger_int-1
	return pivot

def QuickSort(array, first, length):
	comparisons = length - first - 1
	if length - first == 1:
		return comparisons
	else:
		pivot = partition(array, first, length)
		comparisons1 = 0
		if first != pivot:
			comparisons1 = QuickSort(array, first, pivot) 											#sort left part < pivot
		comparisons2 = 0
		if pivot + 1 != length:
			comparisons2 = QuickSort(array, pivot + 1, length) 									#sort right part > pivot
		return comparisons + comparisons1 + comparisons2

file1 = open("unsorted_nums.txt", "r")
array = []
for line in file1:
  array.append(int(line))
file1.close()
length = len(array)
sys.setrecursionlimit(100000)
print("recursion limit:", sys.getrecursionlimit())
num = QuickSort(array, 0, length)
print("comparisons = " , num)
file2 = open("sorted_nums.txt", "w")
for i in array:
	file2.write(str(i)+'\n')
file2.close()