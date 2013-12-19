package com.example.sutdroomsearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllMatchArrayAdapter<T> extends BaseAdapter implements Filterable {

    private List<T> mObjects;
    private final Object mLock = new Object();
    private int mResource;
    private int mDropDownResource;
    private int mFieldId = 0;
    private boolean mNotifyOnChange = true;
    private Context mContext;
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;
    private LayoutInflater mInflater;

    public AllMatchArrayAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0, new ArrayList<T>());
    }

    public AllMatchArrayAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId, new ArrayList<T>());
    }

    public AllMatchArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        init(context, textViewResourceId, 0, Arrays.asList(objects));
    }

    public AllMatchArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        init(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    public AllMatchArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        init(context, textViewResourceId, 0, objects);
    }

    public AllMatchArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        init(context, resource, textViewResourceId, objects);
    }

    public void add(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.add(object);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.add(object);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    public void addAll(Collection<? extends T> collection) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.addAll(collection);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.addAll(collection);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    public void addAll(T ... items) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                for (T item : items) {
                    mOriginalValues.add(item);
                }
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            for (T item : items) {
                mObjects.add(item);
            }
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    public void insert(T object, int index) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.add(index, object);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } else {
            mObjects.add(index, object);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    public void remove(T object) {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.remove(object);
            }
        } else {
            mObjects.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        if (mOriginalValues != null) {
            synchronized (mLock) {
                mOriginalValues.clear();
            }
        } else {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mObjects, comparator);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    private void init(Context context, int resource, int textViewResourceId, List<T> objects) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    public Context getContext() {
        return mContext;
    }

    public int getCount() {
        return mObjects.size();
    }

    public T getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
            int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence)item);
        } else {
            text.setText(item.toString());
        }

        return view;
    }

    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    public static AllMatchArrayAdapter<CharSequence> createFromResource(Context context,
            int textArrayResId, int textViewResId) {
        CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new AllMatchArrayAdapter<CharSequence>(context, textViewResId, strings);
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
		@Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    ArrayList<T> list = new ArrayList<T>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().trim().toLowerCase(Locale.ENGLISH);
                StringBuffer tempString = new StringBuffer();
                for (int i = 0; i < prefixString.length(); i++) {
                	if(prefixString.charAt(i) == ' ') {
                		if (tempString.charAt(tempString.length() - 1) == ' ') {
                			continue;
                		}
                	}
                	tempString.append(prefixString.charAt(i));
                }
                prefixString = tempString.toString();
                
                int levelNumber = findLevelNumber(prefixString);
                int roomNumber = findRoomNumber(prefixString);
                int sectionNumber = findSectionNumber(prefixString);
                
                boolean isModified = true;
                if (roomNumber != -1) {
                	if (levelNumber != -1) {
                		prefixString = "l" + levelNumber + "-r" + roomNumber;
                	} else {
                		prefixString = "r" + roomNumber;
                	}
                } else if (sectionNumber != -1) {
                	if (levelNumber != -1) {
                		prefixString = "l" + levelNumber + "-s" + sectionNumber;
                	} else {
                		prefixString = "s" + sectionNumber;
                	}
                } else if (levelNumber != -1) {
                	prefixString = "l" + levelNumber;
                } else {
                	isModified = false;
                }
                
                final ArrayList<T> values = mOriginalValues;
                final int count = values.size();

                final ArrayList<T> newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase(Locale.ENGLISH);

                    if (isModified) {
                        String[] words = valueText.split("\\|");
                        if (words.length >= 1) {
                        	if (words[0].contains(prefixString)) {
                        			newValues.add(value);
                        	}
                        }
                    } else {
                    	String[] words = valueText.split("\\|");
                    	if (words.length >= 2) {
                    		String[] names = words[1].split(" ");
                    		String[] inputs = prefixString.split(" ");
                    		
                    		boolean[] matches = new boolean[inputs.length];
                    		for (int x = 0; x < inputs.length; x++) {
                    			matches[x] = false;
                    			for (int y = 0; y < names.length; y++) {
                    				if (names[y].startsWith(inputs[x])) {
                    					matches[x] = true;
                    					break;
                    				}
                    			}
                    		}
                    		
                    		boolean allMatches = true;
                    		for (int x = 0; x < matches.length; x++) {
                    			if (!matches[x]) {
                    				allMatches = false;
                    			}
                    		}
                    		
                    		if (allMatches) {
                    			newValues.add(value);
                    		}
                    	}
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
		@Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
        
    	private String findPattern(String reg, String str) {
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return matcher.group();
            } else {
            	return null;
            }
    	}
    	
    	private int findLevelNumber(String str) {
    		String substr = findPattern("((l|f|level|floor|#)[ ]*[0-9]+)|([0-9]+[ ]*(st|nd|rd|th)[ ]*(floor)?)", str);
    		if (substr == null) {
    			return -1;
    		} else {
    			substr = findPattern("[0-9]+", substr);
    			return Integer.parseInt(substr);
    		}
    	}
    	
    	private int findRoomNumber(String str) {
    		String substr = findPattern("[^o][rm][ ]*[0-9]+", str);
    		if (substr != null) {
    			substr = substr.substring(1);
    		} else {
    			substr = findPattern("(^r|(mr)|room)[ ]*[0-9]+", str);
    		}
    		
    		if (substr == null) {
    			return -1;
    		} else {
    			substr = findPattern("[0-9]+", substr);
    			return Integer.parseInt(substr);
    		}
    	}
    	
    	private int findSectionNumber(String str) {
    		String substr = findPattern("(s|section)[ ]*[0-9]+", str);
    		if (substr == null) {
    			return -1;
    		} else {
    			substr = findPattern("[0-9]+", substr);
    			return Integer.parseInt(substr);
    		}
    	}
    }
}
