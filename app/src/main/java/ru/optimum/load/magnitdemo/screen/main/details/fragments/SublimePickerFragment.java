package ru.optimum.load.magnitdemo.screen.main.details.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.text.DateFormat;
import java.util.Locale;

import ru.optimum.load.magnitdemo.R;

public class SublimePickerFragment extends DialogFragment {

    DateFormat mDateFormatter;
    SublimePicker mSublimePicker;
    Callback mCallback;

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            if (mCallback != null) {
                mCallback.onDateTimeRecurrenceSet(selectedDate, hourOfDay, minute, recurrenceOption, recurrenceRule);
            }
            dismiss();
        }

        @Override
        public void onCancelled() {
            if (mCallback != null) {
                mCallback.onCancelled();
            }
            dismiss();
        }
    };

    public SublimePickerFragment () {
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSublimePicker = (SublimePicker) getActivity().getLayoutInflater().inflate(R.layout.sublime_picker, container);

        Bundle arguments = getArguments();
        SublimeOptions options = null;

        if (arguments != null) {
            options = arguments.getParcelable("SUBLIME_OPTIONS");
        }

        mSublimePicker.initializePicker(options, mListener);
        return mSublimePicker;
    }

    public interface Callback {
        void onCancelled();

        void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                     int hourOfDay, int minute,
                                     SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                     String recurrenceRule);
    }
}
