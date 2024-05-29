import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import UsersService from "../service/UsersService";
import {ErrorMessage, Field, Form, Formik, useFormik, useFormikContext} from "formik";
import *  as Yup from "yup";

export default function LoginPage() {
    const [email,setEmail] = useState('')
    const [password,setPassword] = useState('')
    const [error,setError] = useState('')

    const navigate = useNavigate();
    const initialValue = {
        email: '',
        password: ''
    }
    const validateSchema = {
        email: Yup.string().email().required('Email is required'),
        password: Yup.string().required('Password is required')
    }

    const handleSubmit = async (values) => {

        try{
            const userData = await UsersService.login(values.email, values.password)
            if (userData.token){
                localStorage.setItem('token', userData.token)
                localStorage.setItem('role', userData.role)
                localStorage.setItem("refresh_Token",userData.refreshToken)
                navigate('/profile')
            }else{
                console.log(userData)
                setError(userData.error)
            }
        }catch (error) {
            console.log(error)
           if(error.response && error.response.data.errorMessage){
               setError(error.response.data.errorMessage)
           }
        }
    }
return(
    <div className="auth-container">
        <h2>Login</h2>
        {error && <p className="error-message">{error}</p>}
        <Formik initialValues={initialValue}
                onSubmit={(values) => handleSubmit(values)}
                validationSchema={Yup.object(validateSchema)}>
            <Form>
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <Field type="email" name="email" id="email"/>
                    <ErrorMessage name="email" component="span" style={{color: "red"}}/>
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <Field type="password" name="password" id="password"/>
                    <ErrorMessage name="password" component="span" style={{color: "red"}}/>
                </div>
                <div className="form-group">
                    <button type="submit">Login</button>
                </div>
            </Form>
        </Formik>


    </div>
)

}