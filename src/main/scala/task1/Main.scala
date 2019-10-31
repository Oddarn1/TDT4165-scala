package task1

object Main {

  //task 1a
  def array50():List[Int]={
    for {i<-(1 to 50).toList} yield i
  }

  //task 1b
  def sumArray(arr:List[Int]):Int={
    var sum:Int=0
    for (x<-arr){
      sum+=x
    }
    sum
  }

  //task 1c
  def sumArrayRecursive(arr:List[Int],acc:Int):Int={
    if (arr.isEmpty){
      acc
    } else {
      val n = arr.head
      sumArrayRecursive(arr.tail, acc + n)
    }
  }

  //task 1d
  //BigInt has no upper limit of integer precision, as long as enough memory is available.
  def fibRecursive(x1:BigInt,x2:BigInt, n:Int):BigInt={
    if (n==0){
      x2
    }else{
      fibRecursive(x1+x2,x1,n-1)
    }
  }

  def main(args: Array[String]): Unit = {
    val x=array50()
    print("List from 1 to 50: "+x)
    print("\nSum of list above: "+sumArray(x))
    print("\nSum of list calculated recursively: "+sumArrayRecursive(x,0))
    print("\nFibonacci number 10: "+fibRecursive(0,1,10))
  }
}
