package de.woodpot.counterfight;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Thomas on 17.04.2015.
 */
public class EditCounterDialog extends DialogFragment {
    private TextView adviceTextView;
    private EditText counterValueEditText;
    private Button okayButton;
    SessionManager sm;
    Context context;
    FragmentSwitcher fragmentSwitcher;
    private String groupId;

    // Tag-Strings
    private static String TAG_COUNTER_TYPE_EDIT = "edit";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit_counter_dialog, container, false);

        adviceTextView = (TextView)layout.findViewById(R.id.textView_editCounterDialog_advice);
        counterValueEditText = (EditText)layout.findViewById(R.id.editText_editCounterDialog_counterValue);
        okayButton = (Button)layout.findViewById(R.id.button_editCounterDialog_ok);

        okayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int editTextLength = counterValueEditText.getText().toString().length();

                if (editTextLength > 0) {
                    new UpdateCounterValueAsyncTask(context, TAG_COUNTER_TYPE_EDIT, groupId,
                            counterValueEditText.getText().toString()).execute();
                    Log.d("CreateGroupDialog:", "Edit Counter Value: " + counterValueEditText.getText().toString());
                    dismiss();

                }
                else {
                    Toast.makeText(context, "Das Feld darf nicht leer sein!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onActivityCreated(arg0);

        fragmentSwitcher = (FragmentSwitcher) getActivity();
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
