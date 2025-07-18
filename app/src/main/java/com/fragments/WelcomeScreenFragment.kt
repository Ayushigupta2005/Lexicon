package com.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.ayushi.lexicon.R

class WelcomeScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome_screen, container, false)
        val continueBtn = view.findViewById<Button>(R.id.continueBtn).apply {
            startAnimation(
                AnimationUtils.loadAnimation(view.context, R.anim.zoom_in_cut)
            )
        }
        continueBtn.setOnClickListener{
            val action =
                WelcomeScreenFragmentDirections
                    .actionWelcomeScreenFragmentToRobotListScreenFragment()
            findNavController().navigate(action)
        }
        return view
    }


}