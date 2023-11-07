package com.redeyesncode.gozulix.ui.fragments

import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redeyesncode.gozulix.R
import com.redeyesncode.gozulix.databinding.FragmentDialerBinding
import com.redeyesncode.gozulix.room.ContactDatabase
import com.redeyesncode.gozulix.room.ContactEntity
import com.redeyesncode.gozulix.ui.activity.DialerActivity
import com.redeyesncode.gozulix.ui.activity.DisposeCallActivity
import com.redeyesncode.gozulix.ui.activity.SelectContactsActivity
import com.redeyesncode.pay2kart.base.BaseFragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentDialer.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentDialer : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDialerBinding
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var phoneStateListener: PhoneStateListener
    lateinit var pendingContacts:List<ContactEntity>
    private var previousCallState = TelephonyManager.CALL_STATE_IDLE
    lateinit var countDownTimer: CountDownTimer
    private val handler = Handler(Looper.getMainLooper())

    var isStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialerBinding.inflate(inflater,container,false)

        setupRoomDb()
        initClicks()


        return binding.root
    }

    private fun setupRoomDb() {
        val contactDatabase = ContactDatabase.getDatabase(fragmentContext)
        val contactDao = contactDatabase.contactDao()

        pendingContacts = contactDao.getPendingContacts()
        binding.tvPendingCount.text = pendingContacts.size.toString()
        binding.tvDoneCount.text = contactDao.getDoneStatusContacts().size.toString()

        if(pendingContacts.isEmpty()){
            binding.textView.text = "Dialer is Empty"
            binding.tvContactName.text = ""
            binding.tvContactNumber.text = ""
            showMessageDialog("No Contacts in Dialer","DIALER")
        }else{
            // load the service and start on Button click
            binding.textView.text = "Dialing"
            binding.tvContactNumber.text = "Number : ${pendingContacts[0].contactNumber}"
            binding.tvContactName.text = "Name : ${pendingContacts[0].contactName}"



        }




    }

    override fun onResume() {
        super.onResume()
        setupRoomDb()
    }

    private fun setupDialer() {
        // Initialize TelephonyManager
        telephonyManager = activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // Register PhoneStateListener
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String?) {
                showToast(state.toString())
                if (previousCallState == TelephonyManager.CALL_STATE_OFFHOOK &&
                    state == TelephonyManager.CALL_STATE_IDLE
                ) {
                    // Call ended, start your activity here
                    val intent = Intent(fragmentContext,DisposeCallActivity::class.java)
                    intent.putExtra("NUMBER",incomingNumber)
                    startActivity(intent)
                }else if(state==TelephonyManager.CALL_STATE_RINGING){
                    // Call ended, start your activity here
                    val intent = Intent(fragmentContext,DisposeCallActivity::class.java)
                    intent.putExtra("NUMBER",incomingNumber)
                    startActivity(intent)
                }

                previousCallState = state
            }
        }

    }
    fun makePhoneCall(phoneNumber: String) {
        // Check for the CALL_PHONE permission before making the call
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
    private fun initClicks() {
        binding.fabDialer.setOnClickListener {

            val intent = Intent(requireContext(), DialerActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)



        }
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), SelectContactsActivity::class.java)
            startActivityForResult(intent,88)
            activity?.overridePendingTransition(com.redeyesncode.gozulix.R.anim.slide_up, 0)


        }
        binding.btnStartDialer.setOnClickListener {

            if(!isStarted){
                if(pendingContacts.isEmpty()){
                    showMessageDialog("No Contacts in Dialer","DIALER")

                }else{
                    isStarted = true
                    startTimer()
                    binding.btnStartDialer.setBackgroundColor(R.color.yellow)

                }
            }else{
                isStarted = false
                countDownTimer.cancel()
                binding.btnStartDialer.text = "START"
                binding.btnStartDialer.setBackgroundColor(R.color.green_3)

            }



        }
    }


    private fun startTimer() {
        val initialCountdownValue = 10 // Initial countdown value in seconds
        val countdownInterval = 1000 // Interval in milliseconds (1 second)

        countDownTimer = object : CountDownTimer(
            (initialCountdownValue * 1000).toLong(), // Convert seconds to milliseconds
            countdownInterval.toLong()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the TextView with the remaining time
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.btnStartDialer.text = "Dialing in.. ${secondsRemaining.toString()}"
                binding.btnStartDialer.setBackgroundColor(R.color.yellow)
            }

            override fun onFinish() {
                // Countdown has finished, perform any action here
                binding.btnStartDialer.text = "START"
                binding.btnStartDialer.setBackgroundColor(R.color.green_3)

                val intent = Intent(fragmentContext,DisposeCallActivity::class.java)
                intent.putExtra("NUMBER",pendingContacts[0].contactNumber)
                startActivityForResult(intent,77)
                makePhoneCall(pendingContacts[0].contactNumber)

            }
        }


        countDownTimer.start()
    }
    private fun startShakeAnimation(cardView: View) {
        val animationDuration = 500L // Duration for each shake animation
        val repeatDelay = 1000L // Delay between shake animations

        val runnable = object : Runnable {
            override fun run() {
                // Start the shake animation
                shakeCardView(cardView)

                // Repeat the animation with a delay
                handler.postDelayed(this, animationDuration + repeatDelay)
            }
        }

        // Start the first animation
        handler.post(runnable)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==77){
            setupRoomDb()

            if(resultCode==Activity.RESULT_OK){
                val resultData: String = data?.getStringExtra("action")!!
                if(resultData.equals("pause")){
                    countDownTimer.cancel()
                    isStarted = false
                }else if(resultData.equals("close")){
                    countDownTimer.cancel()
                    isStarted = false
                }else{
                    if(pendingContacts.isNotEmpty()){
                        isStarted = true
                        startTimer()
                    }else{
                        countDownTimer.cancel()
                        isStarted = false
                    }

                }
            }
        }else if(requestCode==88){
            setupRoomDb()

        }
    }
    fun shakeCardView(cardView: View) {
        cardView.apply {
            // Define the duration and translation values for the shake animation
            val duration = 100 // Duration in milliseconds
            var translationX = 10f // Horizontal translation in pixels

            // Create the shake animation
            animate()
                .translationXBy(translationX)
                .setDuration(duration.toLong())
                .withEndAction {
                    // Reverse the animation to return the card view to its original position
                    animate()
                        .translationXBy(-translationX * 2) // Move back to the left
                        .setDuration(duration.toLong())
                        .withEndAction {
                            // Reset the translation to zero
                            translationX = 0f
                        }
                }
        }
    }


    override fun onPause() {
        super.onPause()

        // Unregister the PhoneStateListener when the fragment is not visible
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentDialer.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDialer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}