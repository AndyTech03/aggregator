import React, { useEffect, useState } from "react"
import Cookies from 'js-cookie';
import assert from "../utils/assert"

export default function useValue(cookiesName=false, defaultValue=null, useStorage=false) {
	const [value, setValue] = useState(null)
	const [loading, setLoading] = useState(true)
	const save = (data) => {
		const json = JSON.stringify(data)
		if (useStorage) {
			sessionStorage.setItem(cookiesName, json)
		} else {
			Cookies.set(cookiesName, json)
		}
		console.log('saved', data);
	}
	const load = () => {
		if (useStorage) {
			return sessionStorage.getItem(cookiesName)
		} else {
			return Cookies.get(cookiesName)
		}
	}

	const setValueOrDefault = (data) => {
		if (data == null) {
			setValue(defaultValue)
			console.log('set default', defaultValue);
		} else {
			setValue(data)
			console.log('set', data);
		}
	}

	useEffect(() => {
		setValue(prev => {
			try {
				assert(cookiesName != false, 'cookiesName is false')
				assert(loading != false, 'loading is false')
				const data = load()
				assert(data != null, 'data is null')
				const parsed = JSON.parse(data)
				console.log('loaded', parsed)
				return parsed
			} catch (e) {
				console.log(e, 'using default', defaultValue);
				return defaultValue
			} finally {
				setLoading(false)
			}
		})
	}, [cookiesName, setValue, setLoading])

	useEffect(() => {
		try {
			assert(cookiesName != false, 'cookiesName is false')
			assert(loading != true, 'loading is true')
			save(value)
		} catch (e) {
			console.log(e);
		}
	}, [cookiesName, loading, value])

	return [value, setValueOrDefault, save]
}