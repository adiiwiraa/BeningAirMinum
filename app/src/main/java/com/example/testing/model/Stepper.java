package com.example.testing.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testing.R;

public class Stepper extends LinearLayout {
    private int value = 0;
    private TextView valueText;
    private OnValueChangeListener onValueChangeListener;

    public Stepper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.stepper, this, true);
        valueText = findViewById(R.id.valueText);
        Button incrementButton = findViewById(R.id.incrementButton);
        Button decrementButton = findViewById(R.id.decrementButton);

        incrementButton.setOnClickListener(v -> {
            value++;
            valueText.setText(String.valueOf(value));
            if (onValueChangeListener != null) {
                onValueChangeListener.onValueChange(value);
            }
        });

        decrementButton.setOnClickListener(v -> {
            if (value > 0) {
                value--;
                valueText.setText(String.valueOf(value));
                if (onValueChangeListener != null) {
                    onValueChangeListener.onValueChange(value);
                }
            }
        });
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.onValueChangeListener = listener;
    }

    public interface OnValueChangeListener {
        void onValueChange(int value);
    }
}
