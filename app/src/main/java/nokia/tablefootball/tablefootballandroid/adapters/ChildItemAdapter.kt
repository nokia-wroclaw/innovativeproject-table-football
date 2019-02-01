package nokia.tablefootball.tablefootballandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import kotlin.collections.*

class ChildItemAdapter(private val context: Context, var dtosList : ArrayList<TableDTO>)
    : RecyclerView.Adapter<ChildItemAdapter.TableViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val cardView = inflater.inflate(R.layout.list_item, parent, false)

        return TableViewHolder(cardView)

    }

    override fun getItemCount(): Int {
        return dtosList.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val tableDto = dtosList[position]
        holder.bindDto(tableDto)

    }

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var tableImageView: ImageView = itemView.findViewById(R.id.table_imageview) as ImageView
        private var tableStateTextView: TextView = itemView.findViewById(R.id.table_state_textview) as TextView
        private var tableRoomTextView: TextView = itemView.findViewById(R.id.table_room_textview) as TextView

        private var tableDto : TableDTO? = null

        fun bindDto(dto: TableDTO){
            tableDto = dto

            tableImageView.setImageResource(
                when(tableDto!!.online){
                    false -> R.mipmap.table_inactive
                    true -> if(tableDto!!.occupied) R.mipmap.table_occupied else R.mipmap.table_free
                }
            )
            tableImageView.setOnLongClickListener(TableLongClickListener(tableDto!!))

            tableRoomTextView.text = "Room: ${tableDto!!.room.toString()}"
            tableStateTextView.text = if(!tableDto!!.online) "Inactive" else when(tableDto!!.occupied){
                false -> "Free"
                true -> "Occupied"
            }


        }

    }

    class TableLongClickListener(private val tableDto: TableDTO) : View.OnLongClickListener{
        override fun onLongClick(v: View?): Boolean {
            Toast.makeText(v?.context,"Floor ${tableDto.floor} ",Toast.LENGTH_SHORT).show()
            return true
        }

    }



}