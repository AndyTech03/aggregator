import React from "react";
import { createContext } from "react";

export const UserContext = createContext({
	userId: null,
	profile: 'all',
	setProfile: () => {},
})