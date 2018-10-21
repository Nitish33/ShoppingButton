package devnitish.com.shoppingbuttonlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ShoppingButton extends LinearLayout {

    TextView mCount;
    Button mAdd,mRemove;
    ProgressBar progressBar;
    RelativeLayout mCountButtonHolder;

    int count = 0;
    boolean loading;
    boolean isPending = false;
    int max = 100;
    int min = 0;
    int loadTime = 250;
    boolean progressAnimation = true;
    float buttonWeight = 1;
    float countWeight = 2;

    int buttonWidth = -1;
    int buttonHeight = -1;
    int countWith = -1;
    int countHeight=  -1;



    Handler handler;
    Runnable r;

    AddClick addClick;
    RemoveClick removeClick;

    public ShoppingButton(Context context) {
        super(context);
        init();
    }

    public ShoppingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs,R.styleable.ShoppingButton);

        loadTime = array.getInteger(R.styleable.ShoppingButton_animationTime,250);
        max = array.getInteger(R.styleable.ShoppingButton_max,100);
        min = array.getInteger(R.styleable.ShoppingButton_min,0);
        progressAnimation = array.getBoolean(R.styleable.ShoppingButton_showProgressAnimation,true);
        buttonWeight = array.getFloat(R.styleable.ShoppingButton_buttonWeight,1);
        countWeight = array.getFloat(R.styleable.ShoppingButton_countWeight,2);


        buttonWidth = array.getDimensionPixelSize(R.styleable.ShoppingButton_buttonWidth,-1);
        buttonHeight  = array.getDimensionPixelSize(R.styleable.ShoppingButton_buttonHeight,-1);
        countWith = array.getDimensionPixelSize(R.styleable.ShoppingButton_countWidth,-1);
        countHeight = array.getDimensionPixelSize(R.styleable.ShoppingButton_countHeight,-1);

        array.recycle();

        init();
    }

    public ShoppingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);


        TypedArray array = getContext().obtainStyledAttributes(attrs,R.styleable.ShoppingButton);

        loadTime = array.getInteger(R.styleable.ShoppingButton_animationTime,250);
        max = array.getInteger(R.styleable.ShoppingButton_max,100);
        min = array.getInteger(R.styleable.ShoppingButton_min,0);
        progressAnimation = array.getBoolean(R.styleable.ShoppingButton_showProgressAnimation,true);
        buttonWeight = array.getFloat(R.styleable.ShoppingButton_buttonWeight,1);
        countWeight = array.getFloat(R.styleable.ShoppingButton_countWeight,2);

        buttonWidth = array.getDimensionPixelSize(R.styleable.ShoppingButton_buttonWidth,-1);
        buttonHeight  = array.getDimensionPixelSize(R.styleable.ShoppingButton_buttonHeight,-1);
        countWith = array.getDimensionPixelSize(R.styleable.ShoppingButton_countWidth,-1);
        countHeight = array.getDimensionPixelSize(R.styleable.ShoppingButton_countHeight,-1);


        array.recycle();


        init();
    }


    private void init(){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.shopping_button,this);

        mAdd = findViewById(R.id.add);
        mRemove = findViewById(R.id.remove);
        progressBar = findViewById(R.id.progress);
        mCount = findViewById(R.id.count);
        mCountButtonHolder = findViewById(R.id.countButtonHolder);


        setLayoutParam();

        handler = new Handler();


        r = new Runnable() {
            @Override
            public void run() {

                mCount.setVisibility(VISIBLE);
                progressBar.setVisibility(GONE);

            }
        };

        setUpAddButton();
        setUpRemoveButton();
    }

    private void setLayoutParam() {

        LayoutParams addParam = (LayoutParams) mAdd.getLayoutParams();
        LayoutParams removeParam = (LayoutParams) mRemove.getLayoutParams();
        LayoutParams countParam = (LayoutParams) mCountButtonHolder.getLayoutParams();


        addParam.weight = buttonWeight;
        removeParam.weight = buttonWeight;
        countParam.weight = countWeight;


        if(buttonWidth!=-1){
            addParam.width = buttonWidth;
            removeParam.width = buttonWidth;
        }

        if(buttonHeight!=-1){

            addParam.height = buttonHeight;
            removeParam.height =  buttonHeight;
        }

        if(countWith!=-1){
            countParam.width = countWith;
        }

        if(countHeight!=-1){
            countParam.height = countHeight;
        }

        mAdd.setLayoutParams(addParam);
        mRemove.setLayoutParams(removeParam);
        mCountButtonHolder.setLayoutParams(countParam);



    }

    private void setUpRemoveButton() {

        mRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count>min){
                    --count;

                    if(removeClick!=null){

                        removeClick.onRemoveClick(count+1,count,min);
                    }

                    setClickProcedure();
                    handler.postDelayed(r,loadTime);

                }
                else {

                    if(removeClick!=null){

                        removeClick.onRemoveClick(count,count,min);
                    }
                }
            }
        });



    }

    private void setUpAddButton()
    {

        mAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count<max) {
                    ++count;

                    if(addClick != null){
                        addClick.onAddClick(count-1,count,max);
                    }

                    setClickProcedure();
                    handler.postDelayed(r,loadTime);
                }
                else {

                    if(addClick!=null){
                        addClick.onAddClick(count,count,max);
                    }
                }

            }
        });


    }

    private void setClickProcedure(){


        if(progressAnimation) {
            mCount.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);

        }

        mCount.setText(count + "");
    }



    public void setLoading(boolean b){
       loading = b;
    }



    // interfaces.........................................

    public interface AddClick{

        public void onAddClick(int oldCount, int newCount, int max);
    }

    public interface  RemoveClick{

        public void onRemoveClick(int oldCount, int newCount, int min);
    }



    // setter and getter...................
    public void setMax(int max){
        this.max = max;
    }

    public int getMax(){
        return max;
    }

    public void setMin(int min){
        this.min = min;
    }

    public int getMin(){
        return min;
    }

    public void setAnimationTime(int loadTime){
        this.loadTime = loadTime;
    }

    public int getAnimationTime(){
        return loadTime;
    }

    public void showProgressAnimation(boolean b){
        this.progressAnimation = b;
    }



}
