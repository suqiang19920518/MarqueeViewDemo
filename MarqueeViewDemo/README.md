# MyAdTextView
文字滚动广告，可以设置向上和向下滚动



设置向下滚动
setMode(ADTextView.RunMode.DONW);
设置向上滚动
setMode(ADTextView.RunMode.UP);


广告内容点击回调

.setOnAdConetentClickListener(new OnAdConetentClickListener() {
       @Override
       public void OnAdConetentClickListener(int index, AdData data) {
           Toast.makeText(MainActivity.this,data.content,Toast.LENGTH_SHORT).show();
       }
 });
        
    
向上
![image](https://github.com/guiyao/MyAdTextView/blob/master/img/down.gif)

向下
![image](https://github.com/guiyao/MyAdTextView/blob/master/img/up.gif)
      
