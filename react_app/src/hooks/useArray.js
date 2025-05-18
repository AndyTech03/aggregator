import React, { useEffect } from "react"
import useValue from "./useValue";


export default function useArray({
	cookiesName=false,
	defaultValue=[],
	useStorage=false,
	global=false,
	disabled=false,
}) {
	const [items, setItems, saveItems] = useValue({
		cookiesName: cookiesName, 
		defaultValue: defaultValue, 
		useStorage: useStorage,
		global: global,
		disabled: disabled,
	})
	const addItem = (item) => {
		if (disabled)
			throw new Error(`${cookiesName} disabled now!`)
		setItems(prev => [item, ...prev])
	}
	const delItem = (item) => {
		if (disabled)
			throw new Error(`${cookiesName} disabled now!`)
		setItems(prev => prev.filter(c => c !== item))
	}
	const contains = (item) => {
		if (disabled)
			throw new Error(`${cookiesName} disabled now!`)
		if (items == null)
			return false
		return items.includes(item)
	}
	
	return {items, addItem, delItem, setItems, saveItems, contains}
}