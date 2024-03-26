package com.example.adminblinkit.authfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.adminblinkit.R
import com.example.adminblinkit.databinding.FragmentSignInPhoneBinding
import com.example.adminblinkit.utils.Utils

class SignInPhoneFragment : Fragment() {

    private lateinit var binding: FragmentSignInPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInPhoneBinding.inflate(inflater,container,false)

        binding.signBtn.isEnabled=false

        onContinueBtnClick()

        getUserNumber()



        return binding.root
    }

    private fun onContinueBtnClick() {
        binding.signBtn.setOnClickListener(){

            val number = binding.userNo.text.toString()
            if (!(number.length == binding.userNo.length()) || number.isEmpty()){
                Utils.showToast(requireContext(),"Please enter valid number !!")
            }
            else{
                var bundle = Bundle()
                bundle.putString("phoneNumber", number)
                findNavController().navigate(R.id.action_signInPhoneFragment_to_OTPFragment,bundle!!)

            }

        }

    }

    private fun getUserNumber() {
        binding.userNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(number : CharSequence , start: Int, before: Int, count: Int) {
                val len = number?.length
                if (len==binding.userNo.length()){
                    binding.signBtn.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yellow))
                    binding.signBtn.isEnabled=true
                }
                else{
                    binding.signBtn.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


    }


}