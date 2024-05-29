// components/UserManagementPage.js
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import UserService from '../service/UsersService';
import LoadingIcon from "../common/Loading";

function UserManagementPage() {
    const [users, setUsers] = useState([]);

    const [loading, setLoading] = useState(true);
    useEffect(() => {
        // Fetch users data when the component mounts
        fetchUsers();
        setLoading(false)
    }, []);

    const fetchUsers = async () => {
        try {

            const token = localStorage.getItem('token'); // Retrieve the token from localStorage
            const response = await UserService.getAllUsers(token);
            setUsers(response.ourUsersList); // Assuming the list of users is under the key 'ourUsersList'
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };


    const deleteUser = async (userId) => {
        try {
            // Prompt for confirmation before deleting the user
            const confirmDelete = window.confirm('Are you sure you want to delete this user?');

            const token = localStorage.getItem('token'); // Retrieve the token from localStorage
            if (confirmDelete) {
                await UserService.deleteUser(userId, token);
                // After deleting the user, fetch the updated list of users
                fetchUsers();
            }
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };

    return loading ?(<LoadingIcon/>) :(
        <div className="user-management-container">
            <h2>Users Management Page</h2>
            <button className='reg-button'> <Link to="/register">Add User</Link></button>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>
                            <img src={user.img} style={{height: '150px', width: '150px',borderRadius: "50%",objectFit : 'cover'}} />
                        </td>
                        <td>{user.name}</td>
                        <td>{user.email}</td>
                        <td>
                            <button className='delete-button' onClick={() => deleteUser(user.id)}>Delete</button>
                            <button><Link to={`/update-user/${user.id}`}>
                                Update
                            </Link>
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default UserManagementPage;