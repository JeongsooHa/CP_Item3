void main() {  int result;   result =0;   result = func(2,10);  write(result); } int func(int a,int num){  if(num==0){   return 1;  }  return a * func(a,num-1); }
