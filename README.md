# Colony-Counter
Calculates kill curve for cells in a colony assay, 2014

In high school I spent summers as a student intern at Fox Chase Cancer Center. When one of my tasks became counting cell colonies manually I noticed how it could be done much more efficiently by a computer considering the plates were already scanned into the computer for records. I spent several weeks developing this counter as a side project and got input from the people I worked with as I made it, and found to be close to 70% accurate if human counts were considered 100% accurate.

How to use:

1) Ignore the zero correction and average colony size tools as they were never fully developed
Upload a jpeg image

2) To calibrate the image pay attention to the plates that contain the wells for the cell colonys. There are six wells in total, and to calibrate one must first click on the notch in between the top two wells, and the notch in between the two bottom wells. If done correctly a red grid will outline all six wells. 

3) When counted, the cell colonies will be highlighted in red

4) The data displayed is in two columns, the end result it the right column which displays the values for the "kill curve," it considers the bottom right well's colony count to be 100% as that well is commonly the control and has no drug added to it
