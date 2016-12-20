void main()
{
   int arr[10];
   int index;
   int result;
   int i;
   int j;
   int temp;
   int tempIndex;
   int size;
   size = 10;
   temp=0;
   tempIndex=0;
   index=0;
   arr[0] = 10;
   arr[1] = 2;
   arr[2] = 5;
   arr[3] = 9;
   arr[4] = 6;
   arr[5] = 8;
   arr[6] = 7;
   arr[7] = 3;
   arr[8] = 1;
   arr[9] = 4;

   while( index < 10){
      result = arr[index];
      write(result);
      ++index;
   }
   i=0;
   while(i<size){

      j = i + 1;
      while(j<size){
         if(arr[tempIndex]>arr[j]){
            tempIndex=j;
         }
         ++j;
      }
      temp = arr[i];
      arr[i] = arr[tempIndex];
      arr[tempIndex] = temp;

      tempIndex = i + 1;
      ++i;
   }

   index=0;
   while( index < 10){
      result = arr[index];
      write(result);
      ++index;
   }

}

