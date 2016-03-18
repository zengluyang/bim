#include <iostream>
#include <map>
#include <cstdlib>
#include <cstdio>
#include <vector>
#include <list>
using namespace std;


/*
    |  /
    | /
	------
----:x
////:y
||||:z

################## UP_DOWN ##################
topLeft
	   ________
	  /       /
	 /       /
	/_______/ <-downRight

#############################################


############### LEFT_RIGHT ##################

			   /|
			  / |
			 /  |
topLeft->	/   |
			|   |
			|   /  <-downRight
			|  /
			| /
			|/
#############################################



############### FRONT_BOTTOM ################
topLeft
	------
	|    |
	|    |
	------
		downRight
#############################################
*/

enum Direction{
		UP_DOWN,
		LEFT_RIGHT,
		FRONT_BOTTOM
};
class Rectangle;

class Point {
public:
	int x;
	int y;
	int z;
	Point(int x, int y, int z):x(x),y(y),z(z){}
	Point():Point(0,0,0){}
	Point(const Point& point) = default;

    bool operator<(const Point& rhs) const 
    {
        if (x<rhs.x) return true;
        if (x==rhs.x) 
        { 
            if (y<rhs.y) return true;
            if (y==rhs.y) return z<rhs.z;
        }
        return false;
    }

	void moveRightByOnePixel(Direction d) {
		if(d==UP_DOWN) {
			x++;
		} else if(d==LEFT_RIGHT) {
			y++;
		} else if(d==FRONT_BOTTOM) {
			x++;
		}
	}

	void moveDownByOnePixel(Direction d) {
		//std::cout<<"moveDownByOnePixel"<<endl;
		if(d==UP_DOWN) {
			y--;
		} else if(d==LEFT_RIGHT) {
			z--;
		} else if(d==FRONT_BOTTOM) {
			z--;
		}
	}

	void print() const{
		cout<<"Point "<<x<<" "<<y<<" "<<z<<" "<<endl;
	}

	bool overReachRight(Rectangle& rec);
	bool overReachDown(Rectangle& rec);
};




class Rectangle {
public:
	Point topLeft;
	Point topRight;
	Point downLeft;
	Point downRight;
	Direction direction;
	Rectangle(Point topLeft,Point downRight):
		topLeft(topLeft),
		downRight(downRight)
	{
		if(topLeft.x==downRight.x) {
			direction = LEFT_RIGHT;
			topRight = Point(topLeft.x,downRight.y,topLeft.z);
			downLeft = Point(topLeft.x,topLeft.y,downRight.z);
		} else if(topLeft.y==downRight.y) {
			direction = FRONT_BOTTOM;
			topRight = Point(downRight.x,topLeft.y,topLeft.z);
			downLeft = Point(topLeft.x,topLeft.y,downRight.z);
		} else if(topLeft.z==downRight.z) {
			direction = UP_DOWN;
			topRight = Point(downRight.x,topLeft.y,topLeft.z);
			downLeft = Point(topLeft.x,downRight.y,topLeft.z);
		} else {
			cerr<<"Fatal error:未考虑斜面"<<endl;
			::exit(-1);
		}
	}

	void print() const{
		cout<<"topLeft "<<topLeft.x<<" "<<topLeft.y<<" "<<topLeft.z<<" "<<endl;
		cout<<"topRight "<<topRight.x<<" "<<topRight.y<<" "<<topRight.z<<" "<<endl;
		cout<<"downLeft "<<downLeft.x<<" "<<downLeft.y<<" "<<downLeft.z<<" "<<endl;
		cout<<"downRight "<<downRight.x<<" "<<downRight.y<<" "<<downRight.z<<" "<<endl;
	}
};

bool Point::overReachRight(Rectangle& rec) {
	if(rec.direction==UP_DOWN) {
		if(x>=rec.topRight.x) {
			return true;
		}
	} else if(rec.direction==LEFT_RIGHT) {
		if(y>=rec.topRight.y) {
			return true;
		}
	} else if(rec.direction==FRONT_BOTTOM) {
		if(x>=rec.topRight.x) {
			return true;
		}
	}
	return false;
}

bool Point::overReachDown(Rectangle& rec) {
	// cout<<"overReachDown"<<endl;
	if(rec.direction==UP_DOWN) {
		if(y<=rec.downLeft.y) {
			return true;
		}
	} else if(rec.direction==LEFT_RIGHT) {
		if(z<=rec.downLeft.z) {
			return true;
		}
	} else if(rec.direction==FRONT_BOTTOM) {
		// cout<<"rec.direction==FRONT_BOTTOM"<<z<<" "<<rec.downLeft.z<<endl;
		if(z<=rec.downLeft.z) {
			return true;
		}
	}
	// cout<<"return false"<<endl;
	return false;
}

class Cuboid {
public:
	Point topTopLeft;
	Point topTopRight;
	Point topDownLeft;
	Point topDownRight;
	Point downTopLeft;
	Point downTopRight;
	Point downDownLeft;
	Point downDownRight;
};

class Pixel {
public:
	int id;
	static int currentId;
	Point topLeft;
	Direction direction;
	Pixel *upPixel;
	Pixel *downPixel;
	Pixel *leftPixel;
	Pixel *rightPixel;
	Pixel():
		topLeft(0,0,0),
		direction(UP_DOWN),
		upPixel(NULL),
		downPixel(NULL),
		leftPixel(NULL),
		rightPixel(NULL)
	{
		this->id = currentId++;
	}

	Pixel(Point topLeft):
		topLeft(topLeft),
		direction(UP_DOWN),
		upPixel(NULL),
		downPixel(NULL),
		leftPixel(NULL),
		rightPixel(NULL)
	{
		this->id = currentId++;
	}

	bool operator<(const Pixel& rhs) const 
	{
	   return topLeft<rhs.topLeft;
	}

	Pixel getUpPixel() {
		if(direction==UP_DOWN) {
			auto p = Pixel(Point(topLeft.x,topLeft.y+1,topLeft.z));
			p.direction = direction;
			return p;
		} else if(direction==LEFT_RIGHT) {
			auto p = Pixel(Point(topLeft.x,topLeft.y,topLeft.z+1));
			p.direction = direction;
			return p;
		} else if(direction==FRONT_BOTTOM) {
			auto p = Pixel(Point(topLeft.x,topLeft.y,topLeft.z+1));
			p.direction = direction;
			return p;
		}
	}

	Pixel getDownPixel() {
		if(direction==UP_DOWN) {
			auto p = Pixel(Point(topLeft.x,topLeft.y-1,topLeft.z));
			p.direction = direction;
			return p;
		} else if(direction==LEFT_RIGHT) {
			auto p = Pixel(Point(topLeft.x,topLeft.y,topLeft.z-1));
			p.direction = direction;
			return p;
		} else if(direction==FRONT_BOTTOM) {
			auto p = Pixel(Point(topLeft.x,topLeft.y,topLeft.z-1));
			p.direction = direction;
			return p;
		}
	}

	Pixel getLeftPixel() {
		if(direction==UP_DOWN) {
			auto p = Pixel(Point(topLeft.x-1,topLeft.y,topLeft.z));
			p.direction = direction;
			return p;
		} else if(direction==LEFT_RIGHT) {
			auto p = Pixel(Point(topLeft.x,topLeft.y-1,topLeft.z));
			p.direction = direction;
			return p;
		} else if(direction==FRONT_BOTTOM) {
			auto p = Pixel(Point(topLeft.x-1,topLeft.y,topLeft.z));
			p.direction = direction;
			return p;
		}
	}

	Pixel getRightPixel() {
		if(direction==UP_DOWN) {
			auto p = Pixel(Point(topLeft.x+1,topLeft.y,topLeft.z));
			p.direction = direction;
			return p;
		} else if(direction==LEFT_RIGHT) {
			auto p = Pixel(Point(topLeft.x,topLeft.y+1,topLeft.z));
			p.direction = direction;
			return p;
		} else if(direction==FRONT_BOTTOM) {
			auto p = Pixel(Point(topLeft.x+1,topLeft.y,topLeft.z));
			p.direction = direction;
			return p;
		}
	}

	bool isEdge() const{
		return upPixel==NULL || downPixel==NULL || leftPixel==NULL || rightPixel==NULL;
	}

	void print() const{
		cout
			<<"Pixel ["<<id<<"] "
			//<<"["<<this<<"] "
			<<topLeft.x<<" "<<topLeft.y<<" "<<topLeft.z<<" ";
		if(upPixel) {
			cout<<upPixel->id<<" ";
		} else {
			cout<<"NULL ";
		}

		if(downPixel) {
			cout<<downPixel->id<<" ";
		} else {
			cout<<"NULL ";
		}
		
		if(leftPixel) {
			cout<<leftPixel->id<<" ";
		} else {
			cout<<"NULL ";
		}
		
		if(rightPixel) {
			cout<<rightPixel->id<<" ";
		} else {
			cout<<"NULL ";
		}
		cout<<endl;
	}
};



struct CompPointEps {
    bool operator() (const Point& lhs, const Point& rhs) const{
    	//std::cout<<"CompPointEps"<<endl;
    	return 
    		lhs.x<rhs.x ||
    		lhs.y<rhs.y ||
    		lhs.z<rhs.z;

    }
};

int Pixel::currentId = 0;

Pixel* findPixelByPointInPixInMap(map<Point,Pixel*> &pm,Pixel pixel) {
	auto it = pm.find(pixel.topLeft);
	if(it==pm.end()) {
		return NULL;
	}
	return it->second;
}


void testPixel() {
	map<Point,Pixel*> pixelMap;
	pixelMap[Point(0,0,0)]= new Pixel;
	pixelMap[Point(1,0,1)]= new Pixel;
	pixelMap[Point(0,0,1)]= new Pixel;
	pixelMap[Point(0,1,0)]= new Pixel;
	pixelMap[Point(1,0,0)]= new Pixel;
	pixelMap[Point(0,0,0)]= new Pixel;
	pixelMap[Point(4,1,0)]= new Pixel(Point(4,1,0));
	for (auto it = pixelMap.cbegin(); it != pixelMap.cend(); ++it)
	{
		cout<<it->second->topLeft.x<<" "<<it->second->topLeft.y<<" "<<it->second->topLeft.z<<" "<<std::endl; 
	}
	std::cout<<"findPixelByPointInPixInMap(pixelMap,Pixel(Point(4,1,0))) "<<findPixelByPointInPixInMap(pixelMap,Pixel(Point(4,1,0)))<<endl;
	cout<<pixelMap.size()<<endl;
}


void insertRectangleIntoPixelMap(Rectangle& rec, map<Point,Pixel*> &pm){
	Point topToDownPoint = rec.topLeft;
	Direction d = rec.direction;
	cout<<"d "<<d<<endl;
	int i=0;
	for(;!topToDownPoint.overReachDown(rec);topToDownPoint.moveDownByOnePixel(d)) {
		Point currentPoint = topToDownPoint;
		//topToDownPoint.print();
		for(;!currentPoint.overReachRight(rec);currentPoint.moveRightByOnePixel(d)) {
			// currentPoint.print();
			Pixel *p = new Pixel(currentPoint);
			p->direction = d;
			if(findPixelByPointInPixInMap(pm,*p)!=NULL) {
				//cout<<"findPixelByPointInPixInMap(pm,*p)!=NULL"<<endl;
				delete p;
				continue;
			}
			//cout<<i++<<endl;
			// p->print();
			Pixel up = p->getUpPixel();
			Pixel down = p->getDownPixel();
			Pixel left = p->getLeftPixel();
			Pixel right = p->getRightPixel();
			// up.print();
			// down.print();
			// left.print();
			// right.print();
			//p->topLeft.print();
			pm.insert(pair<Point,Pixel*>(p->topLeft,p));
			Pixel* selfInMap = p;
			Pixel* upInMap = findPixelByPointInPixInMap(pm,up);
			Pixel* downInMap = findPixelByPointInPixInMap(pm,down);
			Pixel* leftInMap = findPixelByPointInPixInMap(pm,left);
			Pixel* rightInMap = findPixelByPointInPixInMap(pm,right);

			// std::cout<<upInMap<<endl;
			// std::cout<<downInMap<<endl;
			// std::cout<<leftInMap<<endl;
			// std::cout<<rightInMap<<endl;
			

			if(upInMap) {
				// cout<<"found up! "<<upInMap<<endl;
				upInMap->downPixel = selfInMap;
				// cout<<"upInMap->downPixel = selfInMap;"<<endl;
				selfInMap->upPixel = upInMap;
				// cout<<"selfInMap->upPixel = upInMap;"<<endl;
			}
			if(downInMap) {
				// cout<<"found down!"<<downInMap<<endl;
				downInMap->upPixel = selfInMap;
				selfInMap->downPixel = downInMap;
			}
			if(leftInMap) {
				// cout<<"found left!"<<leftInMap<<endl;
				leftInMap->rightPixel = selfInMap;
				selfInMap->leftPixel = leftInMap;
			}
			if(rightInMap) {
				// cout<<"found right!"<<rightInMap<<endl;
				rightInMap->leftPixel = selfInMap;
				selfInMap->rightPixel = rightInMap;
			}
			
		}
	}
}

typedef vector<Pixel*> RectangleEdge;
typedef vector<RectangleEdge> RectangleEdgeVector;

RectangleEdgeVector recEdges;
list<Pixel*> allEdges;
map<Point,Pixel*> pixelMap;

void extactEdgesFromMap() {
	// allEdges.reserve(pixelMap.size()/3);
	for(auto iter=pixelMap.cbegin();iter!=pixelMap.cend();iter++) {
		if(iter->second->isEdge()) {
			allEdges.push_back(iter->second);
		}
	}
}
static int traverseInSingleEdgeAndSaveEdgeCount = 0;



void divideEdges(){
	while(!allEdges.empty()) {
		Pixel* first= allEdges.front();
		auto head = first;
		auto upInit = first->upPixel;
		auto downInit = first->downPixel;
		auto leftInit = first->leftPixel;
		auto rightInit = first->rightPixel;
		RectangleEdge recEdge;
		Pixel* second;
		if(upInit!=NULL&&upInit->isEdge()) {
			second=upInit;
		}
		if(downInit!=NULL&&downInit->isEdge()) {
			second=downInit;
		} 
		if(leftInit!=NULL&&leftInit->isEdge()) {
			second=leftInit;
		} 
		if(rightInit!=NULL&&rightInit->isEdge()) {
			second=rightInit;
		}  
		while(true) {
			head->print();
			first->print();
			second->print();
			cout<<"cnt "<<traverseInSingleEdgeAndSaveEdgeCount<<" allEdges.size() "<<allEdges.size()<<endl;
			traverseInSingleEdgeAndSaveEdgeCount++;
			cout<<"---------------------------------"<<endl;	
			Pixel* up = second->upPixel;
			Pixel* down = second->downPixel;
			Pixel* left = second->leftPixel;
			Pixel* right = second->rightPixel;
			// std::cout<<"up down left right "<<up<<" "<<down<<" "<<left<<" "<<right<<endl;
			recEdge.push_back(first);
			allEdges.remove(first);
			if(second==head) {
				cout<<"second==head"<<endl;
				break;
			}
			Pixel* old_first = first;
			first=second;
			if(up!=NULL&&up->isEdge()&&up!=old_first) {
				// cout<<"up!=NULL&&up->isEdge()&&up!=old_first"<<endl;
				second=up;
			}
			if(down!=NULL&&down->isEdge()&&down!=old_first) {
				// cout<<"down!=NULL&&down->isEdge()&&down!=old_first"<<endl;
				second=down;
			}
			if(left!=NULL&&left->isEdge()&&left!=old_first) {
				// cout<<"left!=NULL&&left->isEdge()&&left!=old_first"<<endl;
				second=left;
			}
			if(right!=NULL&&right->isEdge()&&right!=old_first) {
				// cout<<"right!=NULL&&right->isEdge()&&right!=first"<<endl;
				second=right;
			}
			// cout<<"333333333"<<endl;
		}
		std::cout<<"22222222"<<endl;
		recEdges.push_back(recEdge);
	}
}

void testRectangle1() {
	Rectangle rec(Point(0,1000,1000),Point(1000,0,1000));
	insertRectangleIntoPixelMap(rec,pixelMap);
}

void testRectangle2() {
	Rectangle rec(Point(0,1000,1000),Point(1000,0,1000));
	insertRectangleIntoPixelMap(rec,pixelMap);
	
}

void testRectangle3() {
	Rectangle rec(Point(0,2000,1000),Point(2000,0,1000));
	insertRectangleIntoPixelMap(rec,pixelMap);
}

void testRectangle4() {
	Rectangle rec(Point(0,1000,0),Point(1000,0,0));
	cout<<"rec.direction "<<rec.direction<<endl;
	insertRectangleIntoPixelMap(rec,pixelMap);
}
void printMap() {
	cout<<pixelMap.size()<<endl;
	int c=0;
	for(auto iter=pixelMap.cbegin();iter!=pixelMap.cend();iter++) {
		iter->second->print();
		//cout<<iter->second->isEdge()<<endl;
		// if(iter->second->isEdge()) {
		// 	iter->second->topLeft.print();
		// 	c++;
		// }
		// if(p.second.rightPixel)
		// 	p.second.rightPixel->print();
		// cout<<endl;
	}
	std::cout<<"c "<<c<<endl;
}

void printAllEdges() {
	cout<<"allEdges.size() "<<allEdges.size()<<endl;
	for(auto iter=allEdges.cbegin();iter!=allEdges.cend();iter++) {
		// (*iter)->print();
	}
	cout<<"-------------------------"<<endl;
}

void printSingleEdges()  {
	cout<<"recEdges.size() "<<recEdges.size()<<endl;
	for(auto iter = recEdges.cbegin();iter!=recEdges.cend();iter++) {
		cout<<"iter->size() "<<iter->size()<<endl;
		for(auto iiter = iter->cbegin();iiter<iter->cend();iiter++) {
			//(*iiter)->print();
		}
	}
}

int main () {
	testPixel();
	testRectangle1();
	testRectangle2();
	testRectangle3();
	testRectangle4();
	//printMap();
	extactEdgesFromMap();
	printAllEdges();
	divideEdges();
	printSingleEdges();
	return 0;
}