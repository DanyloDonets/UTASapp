package donets.danylo.android.utasapp.ui.screens.marschal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import donets.danylo.android.utasapp.R
import donets.danylo.android.utasapp.utilits.APP_ACTIVITY
import donets.danylo.android.utasapp.utilits.replaceFragment

class MarsschalAdapter(private val activity: Activity,
                       private val context: MarschalsFragment,
                       private val marschalId: List<Int>,
                       private val marshalName: List<String>,
                       private val marschalPhone: List<String>,
                       private val marschalRating: ArrayList<String>
): RecyclerView.Adapter<MarsschalAdapter.MaraschalViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaraschalViewHolder {
        val inflater = LayoutInflater.from(APP_ACTIVITY)
        val view = inflater.inflate(R.layout.marschals_list, parent, false)
        return MaraschalViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MaraschalViewHolder, position: Int) {
        holder.marschalId.text = marschalId[position].toString()
        holder.marschalNama.text = marshalName[position]
        holder.marschalPhone.text = marschalPhone[position]
        holder.marschalsRating.text = marschalRating[position].toString()

        holder.fragment.setOnClickListener {

            replaceFragment(EditMarschalFragment(marschalId[position].toString().toInt(),marshalName[position].toString(),marschalPhone[position].toString(),marschalRating[position].toString()),true)
        }
    }

    override fun getItemCount(): Int = marschalId.size

    inner class MaraschalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val marschalId: TextView = itemView.findViewById(R.id.marschal_id)
        val marschalNama: TextView = itemView.findViewById(R.id.marschal_name)
        val marschalPhone: TextView = itemView.findViewById(R.id.marschal_phone)
        val marschalsRating: TextView = itemView.findViewById(R.id.marschal_rating)
        @SuppressLint("ResourceType")
        val fragment: LinearLayout = itemView.findViewById(R.id.RVLayout)

        init {
            // Animate RecyclerView
            val translateAnim = AnimationUtils.loadAnimation(APP_ACTIVITY, R.anim.translate_anim)
            fragment.animation = translateAnim
        }
    }

}