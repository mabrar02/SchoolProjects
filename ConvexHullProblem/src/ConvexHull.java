import java.util.*;

/*
Program that takes in an input for n, then following that it reads in n number of 2 number pairings representing (x,y) coordinates.
The program returns the list of points that would lie on the perimeter of the convex hull in clockwise order.
In this project, we were allowed to assume that n would be an even multiple of three and that none of the x's or y's would be duplicates

Author: Mahdeen Abrar

Example Input:
6
2.1 1
5.2 4
2.5 -3
-3 -3.5
-5 6
0 0

Example Output:
-5.0 6.0
5.2 4.0
2.5 -3.0
-3.0 -3.5
-5.0 6.0

 */
public class ConvexHull {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        //Using a 2D array to represent (x,y) coordinates, the first index represents which point and the second index either x or y
        int n = in.nextInt();
        Double[][] points = new Double[n][2];

        for(int i = 0; i<n; i++) {
            double x = in.nextDouble();
            double y = in.nextDouble();
            points[i][0] = x;
            points[i][1] = y;
        }

        //Collections.sort sorts our list of points in O(nlogn) time
        Arrays.sort(points, Comparator.comparingDouble(o -> o[0]));

        DoublyLinkedList<Double[]> convexHull = convexHull(n, points);
        Node<Double[]> point = convexHull.getHead();


        //Using our point node, we can simply iterate through our doubly linked list and print each point
        do{
            System.out.println(point.getData()[0] + " " + point.getData()[1]);
            point = point.getNext();
        } while (point != convexHull.getHead());

        //Explicitly print the starting node to represent going back to the start
        System.out.println(convexHull.getHead().getData()[0] + " " + convexHull.getHead().getData()[1]);


    }

    /**
     * Recursively finds the convex hull of n points where n is 3 times a power of 2
     * @param numPoints the number of points to find a convex hull around
     * @param listOfPoints list of (x,y) coordinate pair points
     * @return doublylinked list with all the points on the perimeter of the convex hull
     */
    public static DoublyLinkedList<Double[]> convexHull(int numPoints, Double[][] listOfPoints){
        /*
        Base case: If we have 3 points, we know all the points belong to the convex hull so we simply must order them correctly
        and return a DLL (Doubly Linked List)
         */
        if(numPoints == 3){
            DoublyLinkedList<Double[]> hull = new DoublyLinkedList<>();
            hull.addLast(listOfPoints[0]); //B/c our list is sorted by x, we know our first point will always come first in our DLL

            /*
            Given the points (a,b), (c,d) and (e,f), we can determine whether our points travel in counterclockwise order
            or not. if (f-b)(c-a)>(d-b)(e-a), then we know it is clockwise
            SOURCE: http://jeffe.cs.illinois.edu/teaching/373/notes/x05-convexhull.pdf
             */

            double num1 = (listOfPoints[2][1] - listOfPoints[0][1])*(listOfPoints[1][0] - listOfPoints[0][0]);
            double num2 = (listOfPoints[1][1] - listOfPoints[0][1])*(listOfPoints[2][0] - listOfPoints[0][0]);

            /*
            If our 3 points are CCW, then we know the 3rd point comes second in CW rotation, if our 3 points are not
            CCW, then we know our 2nd point comes second in CW rotation
             */
            if(num1 > num2){
                hull.addLast(listOfPoints[2]);
                hull.addLast(listOfPoints[1]);
            }
            else{
                hull.addLast(listOfPoints[1]);
                hull.addLast(listOfPoints[2]);
            }

            hull.makeCircular();
            return hull;
        }

        //If we don't have our base case, we split our points up into left and right convex hulls until we are left with 3 point segments (BASE CASE)
        int n = numPoints/2;
        Double[][] leftList = new Double[n][2];
        Double[][] rightList = new Double[n][2];

        /*
        listOfPoints is sorted, so we take the first half into the left, and the second half into the right
         */
        for(int i = 0; i<n; i++){
            leftList[i][0] = listOfPoints[i][0];
            leftList[i][1] = listOfPoints[i][1];
        }
        for(int i = n; i<numPoints; i++){
            rightList[i-n][0] = listOfPoints[i][0];
            rightList[i-n][1] = listOfPoints[i][1];
        }

        //Recursively call and get a left and right convex
        DoublyLinkedList<Double[]> leftConvex = convexHull(n, leftList);
        DoublyLinkedList<Double[]> rightConvex = convexHull(n, rightList);

        /*
        P will represent the point on our left CH were the upper vertex connects and Q will represent the point on our
        right CH where the upper vertex connects. We start off p and q to be the closest points to one another, meaning
        p = rightmost point and q = leftmost point
         */
        Node<Double[]> p = leftConvex.getHead();
        for(int i = 0; i<n; i++){
            //Finding the rightmost point by seeing greatest x
            if(p.getData()[0] < p.getNext().getData()[0]){
                p = p.getNext();
            }
        }
        Node<Double[]> q = rightConvex.getHead();
        for(int i = 0; i<n; i++){
            //Finding leftmost point by seeing smallest x
            if(q.getNext().getData()[0] < q.getData()[0]){
                q = q.getNext();
            }
        }

        /*
        Pseudocode provided in the assignment instructions. If right hull point shift results in CCW, we follow through with shift,
        if left hull point shift results in CW, we follow through with the shift. If we iterate through and find that neither point
        shifts, then we move on with our code
         */
        boolean flag = true;
        while(flag){
            flag = false;

            if(direction(p.getData(), q.getNext().getData(), q.getData()) == -1){
                q = q.getNext();
                flag = true;
            }

            if(direction(q.getData(), p.getPrev().getData(), p.getData()) == 1){
                p = p.getPrev();
                flag = true;
            }
        }

        /*
        P2 will represent the point on our left CH were the lower vertex connects and Q2 will represent the point on our
        right CH where the lower vertex connects. We start off p2 and q2 to be the closest points to one another, meaning
        p2 = rightmost point and q2 = leftmost point
         */
        Node<Double[]> p2 = leftConvex.getHead();
        for(int i = 0; i<n; i++){
            if(p2.getData()[0] < p2.getNext().getData()[0]){
                p2 = p2.getNext();
            }
        }
        Node<Double[]> q2 = rightConvex.getHead();
        for(int i = 0; i<n; i++){
            if(q2.getNext().getData()[0] < q2.getData()[0]){
                q2 = q2.getNext();
            }
        }

        /*
        Slightly altered from above as we are looking for a lower tangent instead. This time, if p2 shift results in
        CW, we follow through and if q2 shift results in CCW, we follow through
         */
        flag = true;
        while(flag){
            flag = false;

            if(direction(p2.getData(), q2.getPrev().getData(), q2.getData()) == 1){
                q2 = q2.getPrev();
                flag = true;
            }

            if(direction(q2.getData(), p2.getNext().getData(), p2.getData()) == -1){
                p2 = p2.getNext();
                flag = true;
            }
        }
        /*
        At this point, we now have the points where the upper tangent connects and the lower tangent connects. We will now
        make a new convex hull list which will contain the points on the perimeter of our hull
         */
        DoublyLinkedList<Double[]> returnHull = new DoublyLinkedList<>();
        Node<Double[]> iterator = leftConvex.getHead();

        /*
        Starting at our leftmost point overall, we retrieve each point until we reach p (our first point of our upper tangent).
        We then jump straight to q (our second point of our upper tangent) and continue retrieving until we reach q2
        (our first point of our lower tangent). We then again jump to p2 (our second point of our lower tangent) and retrieve
        until we are back at the beginning of our hull.
         */
        do {
            returnHull.addLast(iterator.getData());
            iterator = iterator.getNext();
        } while (!Objects.equals(iterator.getPrev().getData()[0], p.getData()[0]));

        iterator = q;

        do {
            returnHull.addLast(iterator.getData());
            iterator = iterator.getNext();
        } while (!Objects.equals(iterator.getPrev().getData()[0], q2.getData()[0]));

        //We require and if here because if p2 happens to be our starting point, it would retrieve twice
        if(!Objects.equals(p2.getData()[0], leftConvex.getHead().getData()[0])){
            iterator = p2;

            do {
                returnHull.addLast(iterator.getData());
                iterator = iterator.getNext();
            } while (!Objects.equals(iterator.getData()[0], leftConvex.getHead().getData()[0]));

        }

        returnHull.makeCircular();
        return returnHull;
    }

    /**
     * Function used to determine the direction of a point from a line segment to help us determine how to move our upper
     * and lower vertices.
     * This code was and idea was modified from: https://www.geeksforgeeks.org/direction-point-line-segment/
     * @param a the point of the opposite hull
     * @param b the predecessor/successor of the hull of which point we are checking
     * @param p the point we are checking to see which direction
     * @return -1 if the point is to the left of the line, 1 if it is to the right
     */
    public static int direction(Double[] a, Double[] b, Double[] p){
        double ax, ay, bx, by, px, py;
        bx = b[0];
        by = b[1];
        ax = a[0];
        ay = a[1];
        px = p[0];
        py = p[1];

        //We use point a as our origin to make calculations simpler
        bx -= ax;
        by -= ay;
        px -= ax;
        py -= ay;

        //Once we get the cross product of our b and p points, we can compare that and determine whether it is left or right
        double crossProduct = bx*py - by*px;
        if(crossProduct > 0){
            return 1; //right
        }
        else if(crossProduct < 0){
            return -1; //left
        }
        else return 0;
    }

}
