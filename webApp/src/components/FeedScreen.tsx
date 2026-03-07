import React from 'react';
import { Link } from 'react-router-dom';

const FeedScreen: React.FC = () => {
  return (
    <div className="flex h-screen overflow-hidden bg-background-light dark:bg-background-dark text-slate-900 dark:text-slate-100 font-display">
      {/* Left Sidebar Navigation */}
      <aside className="w-72 border-r border-slate-200 dark:border-slate-800 flex flex-col p-6 gap-8 bg-background-light dark:bg-background-dark z-20">
        <div className="flex items-center gap-3 px-2">
          <div className="bg-primary p-2 rounded-xl flex items-center justify-center text-white">
            <span className="material-symbols-outlined">auto_awesome</span>
          </div>
          <h1 className="text-xl font-extrabold tracking-tight">Synapse</h1>
        </div>
        <nav className="flex flex-col gap-2">
          <Link className="flex items-center gap-4 px-4 py-3 rounded-xl bg-primary/10 text-primary font-semibold transition-all" to="/feed">
            <span className="material-symbols-outlined">home</span>
            <span>Home</span>
          </Link>
          <Link className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/50 text-slate-600 dark:text-slate-400 transition-all font-semibold" to="/chat">
            <span className="material-symbols-outlined">chat_bubble</span>
            <span>Messages</span>
          </Link>
          <Link className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/50 text-slate-600 dark:text-slate-400 transition-all font-semibold" to="/profile">
            <span className="material-symbols-outlined">person</span>
            <span>Profile</span>
          </Link>
          <a className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/50 text-slate-600 dark:text-slate-400 transition-all" href="#">
            <span className="material-symbols-outlined">explore</span>
            <span>Search</span>
          </a>
          <a className="flex items-center gap-4 px-4 py-3 rounded-xl hover:bg-slate-100 dark:hover:bg-slate-800/50 text-slate-600 dark:text-slate-400 transition-all" href="#">
            <span className="material-symbols-outlined">notifications</span>
            <span>Notifications</span>
          </a>
        </nav>
        <div className="mt-auto">
          <button className="w-full bg-primary hover:bg-primary/90 text-white font-bold py-4 rounded-xl shadow-lg shadow-primary/20 flex items-center justify-center gap-2">
            <span className="material-symbols-outlined">add_circle</span>
            New Post
          </button>
        </div>
      </aside>

      {/* Main Feed Column */}
      <main className="flex-1 overflow-y-auto bg-slate-50 dark:bg-[#0c0c1a] relative">
        {/* Background Glows */}
        <div className="absolute top-0 left-1/4 w-96 h-96 bg-primary/10 rounded-full blur-[120px] pointer-events-none"></div>
        <div className="absolute bottom-1/4 right-1/4 w-64 h-64 bg-violet-500/10 rounded-full blur-[100px] pointer-events-none"></div>
        
        {/* Top Header */}
        <header className="sticky top-0 z-10 bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border-b border-white/20 dark:border-slate-800/50 px-8 py-4 flex items-center justify-between">
          <div className="relative w-full max-w-md">
            <span className="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">search</span>
            <input className="w-full bg-slate-200/50 dark:bg-slate-800/50 border-none rounded-xl pl-11 pr-4 py-2 focus:ring-2 focus:ring-primary/50 text-sm outline-none" placeholder="Search the synapse..." type="text"/>
          </div>
          <div className="flex items-center gap-4">
            <button className="p-2 rounded-lg hover:bg-slate-200 dark:hover:bg-slate-800 text-slate-500 transition-colors">
              <span className="material-symbols-outlined">settings</span>
            </button>
            <div className="h-10 w-10 rounded-full bg-slate-300 dark:bg-slate-700 bg-cover bg-center border-2 border-primary/30 cursor-pointer"></div>
          </div>
        </header>

        <div className="max-w-2xl mx-auto py-8 px-4 flex flex-col gap-6">
          {/* Create Post Card */}
          <div className="bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border border-white/20 dark:border-slate-800/50 rounded-2xl p-6 flex flex-col gap-4 shadow-sm">
            <div className="flex gap-4">
              <div className="h-12 w-12 rounded-full bg-slate-400 shrink-0"></div>
              <textarea className="w-full bg-transparent border-none focus:ring-0 resize-none text-lg placeholder:text-slate-500 pt-2 outline-none" placeholder="What's sparking in your mind?"></textarea>
            </div>
            <div className="flex items-center justify-between border-t border-slate-200 dark:border-slate-800 pt-4">
              <div className="flex gap-2">
                <button className="p-2 text-primary hover:bg-primary/10 rounded-lg transition-colors"><span className="material-symbols-outlined">image</span></button>
                <button className="p-2 text-primary hover:bg-primary/10 rounded-lg transition-colors"><span className="material-symbols-outlined">poll</span></button>
                <button className="p-2 text-primary hover:bg-primary/10 rounded-lg transition-colors"><span className="material-symbols-outlined">sentiment_satisfied</span></button>
              </div>
              <button className="px-6 py-2 bg-primary text-white font-bold rounded-lg text-sm hover:opacity-90 transition-opacity">Post</button>
            </div>
          </div>

          {/* Example Image Post */}
          <article className="bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border border-white/20 dark:border-slate-800/50 rounded-2xl overflow-hidden group hover:border-primary/30 transition-all duration-300 shadow-sm">
            <div className="p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-3">
                  <div className="h-10 w-10 rounded-full bg-slate-400"></div>
                  <div>
                    <h4 className="font-bold text-sm">Elena Vance</h4>
                    <p className="text-xs text-slate-500">2 hours ago • Neural Designer</p>
                  </div>
                </div>
                <button className="text-slate-400"><span className="material-symbols-outlined">more_horiz</span></button>
              </div>
              <p className="text-slate-700 dark:text-slate-300 mb-4 leading-relaxed">
                Just finished exploring the new spatial UI paradigms for the upcoming Synapse update. The way we interact with information is about to become more intuitive than ever. 🌌 #DesignFuture #SpatialComputing
              </p>
            </div>
            <div className="w-full h-80 bg-slate-800 flex items-center justify-center text-slate-500 italic">
               Design Visual Loaded Here
            </div>
            <div className="p-4 px-6 flex items-center justify-between border-t border-slate-200 dark:border-slate-800">
              <div className="flex gap-6">
                <button className="flex items-center gap-2 text-slate-500 hover:text-primary transition-colors group/btn">
                  <span className="material-symbols-outlined group-hover/btn:scale-110 transition-transform">favorite</span>
                  <span className="text-sm font-medium">1.2k</span>
                </button>
                <button className="flex items-center gap-2 text-slate-500 hover:text-primary transition-colors group/btn">
                  <span className="material-symbols-outlined group-hover/btn:scale-110 transition-transform">chat_bubble</span>
                  <span className="text-sm font-medium">84</span>
                </button>
                <button className="flex items-center gap-2 text-slate-500 hover:text-primary transition-colors group/btn">
                  <span className="material-symbols-outlined group-hover/btn:scale-110 transition-transform">share</span>
                  <span className="text-sm font-medium">12</span>
                </button>
              </div>
              <button className="text-slate-500 hover:text-primary"><span className="material-symbols-outlined">bookmark</span></button>
            </div>
          </article>
        </div>
      </main>

      {/* Right Sidebar */}
      <aside className="w-80 border-l border-slate-200 dark:border-slate-800 hidden lg:flex flex-col p-6 gap-8 bg-background-light dark:bg-background-dark overflow-y-auto">
        <div className="p-5 bg-white/40 dark:bg-slate-900/40 backdrop-blur-xl border border-primary/20 rounded-2xl relative overflow-hidden group">
          <div className="absolute -top-4 -right-4 w-20 h-20 bg-primary/20 rounded-full blur-2xl"></div>
          <div className="flex items-center gap-2 mb-3">
            <span className="material-symbols-outlined text-primary text-xl">auto_fix_high</span>
            <h3 className="font-bold text-sm tracking-tight">Synapse AI Assistant</h3>
          </div>
          <p className="text-xs text-slate-500 leading-relaxed mb-4">
            I've curated some articles about spatial UI that might interest you.
          </p>
          <button className="w-full py-2.5 bg-primary/10 hover:bg-primary/20 text-primary text-xs font-bold rounded-lg transition-colors">
            View Personalized Insights
          </button>
        </div>

        <div className="flex flex-col gap-4">
          <h3 className="text-sm font-bold uppercase tracking-widest text-slate-500 px-2">Trending Now</h3>
          <div className="flex flex-col gap-1">
            <div className="p-3 hover:bg-slate-100 dark:hover:bg-slate-800/50 rounded-xl transition-all cursor-pointer group">
              <p className="text-[10px] text-slate-500 uppercase">Technology • Trending</p>
              <h4 className="text-sm font-bold group-hover:text-primary transition-colors">#QuantumComputing</h4>
              <p className="text-[10px] text-slate-500 mt-1">12.5k Synapses</p>
            </div>
          </div>
        </div>
      </aside>
    </div>
  );
};

export default FeedScreen;
