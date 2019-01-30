package nokia.tablefootball.tablefootballandroid.activity.helpers

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.dto.TableDTO
import nokia.tablefootball.tablefootballandroid.utils.TableDataUtil
import java.util.*
import kotlin.collections.HashSet

class FloorListAdapter(
    private val context: Context,
    private val tableDtos: List<TableDTO>

) : BaseExpandableListAdapter() {

    private val tablesMap: TreeMap<Int, ArrayList<TableDTO>> = TableDataUtil.toFloorMap(tableDtos)

    private val expandableListDetail: TreeMap<String, List<String>> = TableDataUtil.toFloorMapAsStrings(tableDtos)
    private val expandableListTitle: List<String>

    init{
        expandableListTitle = expandableListDetail.keys.toList()
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {

        val result = tablesMap[listPosition]!!


        return this.expandableListDetail[this.expandableListTitle[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView as View
        val expandedListText = getChild(listPosition, expandedListPosition) as String

        if (convertView == null) {
            val layoutInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }

        val expandedListTextView = convertView!!
            .findViewById<View>(R.id.list_item) as TextView

        expandedListTextView.text = expandedListText

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]!!
            .size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.floor_list, null)
        }
        val listTitleTextView = convertView!!
            .findViewById<View>(R.id.listTitle) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
