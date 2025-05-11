import React, { useEffect } from "react"
import useValue from "./useValue";


export default function useArray({
	cookiesName=false, defaultValue=[], useStorage=false, global=false,
}) {
	const [items, setItems, saveItems] = useValue({
		cookiesName: cookiesName, 
		defaultValue: defaultValue, 
		useStorage: useStorage,
		global: global,
	})
	const addItem = (item) => {
		setItems(prev => [item, ...prev])
	}
	const delItem = (item) => {
		setItems(prev => prev.filter(c => c !== item))
	}
	const contains = (item) => items.includes(item)
	
	return {items, addItem, delItem, setItems, saveItems, contains}
}