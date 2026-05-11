package com.example.mahasale.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.mahasale.R

class InboxFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_inbox, container, false)
}
