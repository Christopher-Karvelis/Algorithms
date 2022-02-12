import sys

def merge(left_array, right_array):
	i = 0
	j = 0
	global inversions
	merged_array = []
	merged_size = len(left_array) + len(right_array)
	for k in range(0, merged_size):
		if i == len(left_array):
			merged_array.append(right_array[j])
			j += 1
		elif j == len(right_array):
			merged_array.append(left_array[i])
			i += 1
		elif left_array[i] < right_array[j]:
			merged_array.append(left_array[i])
			i += 1
		else:
			merged_array.append(right_array[j])
			inversions = inversions + (len(left_array) - i)
			j += 1 
	return merged_array

def mergesort(array):
	length = len(array)
	if length == 1:
		return array
	else:
		mid_pointer = length // 2
		left_array = array[:mid_pointer]
		right_array = array[mid_pointer:]
		left_sorted_array = mergesort(left_array)
		right_sorted_array = mergesort(right_array)
		merged_array = merge(left_sorted_array, right_sorted_array)
		return merged_array
		
array = []
inversions = 0
file1 = open("unsorted_nums.txt", "r")
for line in file1:
  array.append(int(line))
file1.close()
sys.setrecursionlimit(100000)
print("recursion limit:", sys.getrecursionlimit())
sorted_array = mergesort(array)
print("inversions = ", inversions)
file2 = open("sorted_nums.txt", "w")
for i in sorted_array:
	file2.write(str(i)+'\n')
file2.close()