
import { useState } from "react";
import "./App.css";

function App() {

  // =========================
  // LOGIN / REGISTER
  // =========================

  const [isLogin, setIsLogin] = useState(true);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [loggedIn, setLoggedIn] = useState(false);

  // =========================
  // JOURNAL
  // =========================

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [journals, setJournals] = useState([]);

  const [editingId, setEditingId] = useState(null);


  // =========================
  // REGISTER
  // =========================

  const handleRegister = async () => {

    if (!username.trim() || !password.trim()) {
      alert("Please enter username and password");
      return;
    }

    try {

      const response = await fetch(
        "http://localhost:8081/user",
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json"
          },

          body: JSON.stringify({
            username: username,
            password: password
          })
        }
      );

      if (response.status === 201) {

        alert("Registration successful! Now login.");

        setPassword("");
        setIsLogin(true);

      } else {

        alert(
          "Registration failed. Status: " +
          response.status
        );
      }

    } catch (error) {

      console.error("Register error:", error);

      alert("Backend se connection nahi ho raha.");
    }
  };


  // =========================
  // LOGIN
  // =========================

 const handleLogin = async () => {

  if (!username.trim() || !password.trim()) {
    alert("Please enter username and password");
    return;
  }

  const authHeader =
    "Basic " + btoa(username + ":" + password);

  try {

    const response = await fetch(
  `http://localhost:8081/journalget/${username}`,
  {
    method: "GET",

    headers: {
      "Authorization": authHeader
    }
  }
);

    if (response.ok || response.status === 404) {

      setLoggedIn(true);

      fetchJournals(username, authHeader);

    } else if (response.status === 401) {

      alert("Wrong username or password");

    } else {

      alert(
        "Login failed. Status: " +
        response.status
      );
    }

  } catch (error) {

    console.error("Login error:", error);

    alert("Backend se connection nahi ho raha.");
  }
};
  // =========================
  // GET JOURNALS
  // =========================

  const fetchJournals = async (
    currentUsername = username,
    currentAuthHeader =
      "Basic " + btoa(username + ":" + password)
  ) => {

    try {

      const response = await fetch(
        `http://localhost:8081/journalget/${currentUsername}`,
        {
          method: "GET",

          headers: {
            "Authorization": currentAuthHeader
          }
        }
      );

      if (response.ok) {

        const data = await response.json();

        setJournals(data);

      } else if (response.status === 404) {

        setJournals([]);

      } else {

        console.log(
          "Failed to fetch journals:",
          response.status
        );
      }

    } catch (error) {

      console.error("Fetch error:", error);
    }
  };


  // =========================
  // SAVE JOURNAL
  // =========================

  const handleSave = async () => {

    if (!title.trim() || !content.trim()) {
      alert("Please enter title and content");
      return;
    }

    const authHeader =
      "Basic " + btoa(username + ":" + password);

    try {

      const response = await fetch(
        `http://localhost:8081/journalpost/${username}`,
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json",
            "Authorization": authHeader
          },

          body: JSON.stringify({
            title: title,
            content: content
          })
        }
      );

      if (response.ok) {

        alert("Journal saved successfully!");

        setTitle("");
        setContent("");

        fetchJournals(username, authHeader);

      } else {

        alert(
          "Failed to save journal. Status: " +
          response.status
        );
      }

    } catch (error) {

      console.error("Save error:", error);

      alert("Backend se connection nahi ho raha.");
    }
  };


  // =========================
  // EDIT JOURNAL
  // =========================

  const handleEdit = (journal) => {

    setEditingId(journal.id);
    setTitle(journal.title);
    setContent(journal.content);

    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  };


  // =========================
  // UPDATE JOURNAL
  // =========================

  const handleUpdate = async () => {

    if (!title.trim() || !content.trim()) {
      alert("Please enter title and content");
      return;
    }

    const authHeader =
      "Basic " + btoa(username + ":" + password);

    try {

      const response = await fetch(
        `http://localhost:8081/id/${username}/${editingId}`,
        {
          method: "PUT",

          headers: {
            "Content-Type": "application/json",
            "Authorization": authHeader
          },

          body: JSON.stringify({
            title: title,
            content: content
          })
        }
      );

      if (response.ok) {

        alert("Journal updated successfully!");

        setTitle("");
        setContent("");
        setEditingId(null);

        fetchJournals(username, authHeader);

      } else {

        alert(
          "Update failed. Status: " +
          response.status
        );
      }

    } catch (error) {

      console.error("Update error:", error);

      alert("Backend se connection nahi ho raha.");
    }
  };


  // =========================
  // CANCEL EDIT
  // =========================

  const handleCancelEdit = () => {

    setEditingId(null);
    setTitle("");
    setContent("");
  };


  // =========================
  // DELETE JOURNAL
  // =========================

  const handleDelete = async (id) => {

    const authHeader =
      "Basic " + btoa(username + ":" + password);

    try {

      const response = await fetch(
        `http://localhost:8081/id/${username}/${id}`,
        {
          method: "DELETE",

          headers: {
            "Authorization": authHeader
          }
        }
      );

      if (response.ok) {

        alert("Journal deleted!");

        fetchJournals(username, authHeader);

      } else {

        alert(
          "Delete failed. Status: " +
          response.status
        );
      }

    } catch (error) {

      console.error("Delete error:", error);
    }
  };


  // =========================
  // LOGOUT
  // =========================

  const handleLogout = () => {

    setLoggedIn(false);

    setUsername("");
    setPassword("");

    setTitle("");
    setContent("");
    setJournals([]);
    setEditingId(null);
  };


  // =========================
  // LOGIN / REGISTER
  // =========================

  if (!loggedIn) {

    return (
      <div className="auth-page">

        <div className="auth-container">

          <div className="logo">
            📔
          </div>

          <h1>My Journal</h1>

          <p className="subtitle">
            Your thoughts, your private space.
          </p>

          {isLogin ? (

            <>
              <h2>Welcome Back</h2>

              <p className="form-subtitle">
                Login to continue to your journal
              </p>

              <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) =>
                  setUsername(e.target.value)
                }
              />

              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) =>
                  setPassword(e.target.value)
                }
              />

              <button
                className="primary-btn"
                onClick={handleLogin}
              >
                Login
              </button>

              <p className="switch-text">
                Don't have an account?
              </p>

              <button
                className="secondary-btn"
                onClick={() => setIsLogin(false)}
              >
                Create New Account
              </button>
            </>

          ) : (

            <>
              <h2>Create Account</h2>

              <p className="form-subtitle">
                Start your private journal today
              </p>

              <input
                type="text"
                placeholder="Choose Username"
                value={username}
                onChange={(e) =>
                  setUsername(e.target.value)
                }
              />

              <input
                type="password"
                placeholder="Choose Password"
                value={password}
                onChange={(e) =>
                  setPassword(e.target.value)
                }
              />

              <button
                className="primary-btn"
                onClick={handleRegister}
              >
                Register
              </button>

              <p className="switch-text">
                Already have an account?
              </p>

              <button
                className="secondary-btn"
                onClick={() => setIsLogin(true)}
              >
                Login
              </button>
            </>
          )}

        </div>

      </div>
    );
  }


  // =========================
  // DASHBOARD
  // =========================

  return (
    <div className="dashboard">

      <header className="header">

        <div>
          <h1>📔 My Journal</h1>
          <p>Welcome back, <strong>{username}</strong></p>
        </div>

        <button
          className="logout-btn"
          onClick={handleLogout}
        >
          Logout
        </button>

      </header>


      {/* JOURNAL FORM */}

      <section className="journal-form">

        <h2>
          {editingId
            ? "✏️ Edit Journal"
            : "📝 Create New Journal"}
        </h2>

        <input
          type="text"
          placeholder="Journal title"
          value={title}
          onChange={(e) =>
            setTitle(e.target.value)
          }
        />

        <textarea
          placeholder="Write your thoughts here..."
          value={content}
          onChange={(e) =>
            setContent(e.target.value)
          }
        />

        <div className="form-buttons">

          {editingId ? (

            <>
              <button
                className="primary-btn"
                onClick={handleUpdate}
              >
                Update Journal
              </button>

              <button
                className="cancel-btn"
                onClick={handleCancelEdit}
              >
                Cancel
              </button>
            </>

          ) : (

            <button
              className="primary-btn"
              onClick={handleSave}
            >
              Save Journal
            </button>

          )}

        </div>

      </section>


      {/* JOURNALS */}

      <section className="journal-list">

        <div className="section-heading">

          <h2>📚 My Journals</h2>

          <span>
            {journals.length}{" "}
            {journals.length === 1
              ? "entry"
              : "entries"}
          </span>

        </div>

        {journals.length === 0 ? (

          <div className="empty-state">

            <div className="empty-icon">
              📖
            </div>

            <h3>No journals yet</h3>

            <p>
              Start writing your first journal entry above.
            </p>

          </div>

        ) : (

          journals.map((journal) => (

            <article
              className="journal-card"
              key={journal.id}
            >

              <div className="journal-content">

                <h3>{journal.title}</h3>

                <p>{journal.content}</p>

              </div>

              <div className="journal-actions">

                <button
                  className="edit-btn"
                  onClick={() =>
                    handleEdit(journal)
                  }
                >
                  ✏️ Edit
                </button>

                <button
                  className="delete-btn"
                  onClick={() =>
                    handleDelete(journal.id)
                  }
                >
                  🗑️ Delete
                </button>

              </div>

            </article>

          ))

        )}

      </section>

    </div>
  );
}

export default App;

