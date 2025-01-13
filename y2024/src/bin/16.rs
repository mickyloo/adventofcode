use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use std::collections::HashSet;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "16"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############";

const TEST2: &str = "\
#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################";

enum Dir {
    N,
    S,
    E,
    W,
}

struct State {
    pos: (usize, usize),
    dir: Dir,
    score: usize,
}

struct State2 {
    pos: (usize, usize),
    dir: Dir,
    score: usize,
    path: HashSet<(usize, usize)>,
}

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R) -> Result<(usize, Vec<Vec<usize>>)> {
        let grid = read_to_2d(reader);
        let bounds = grid.len();

        let start = (bounds - 2, 1_usize);
        let end = (1_usize, bounds - 2);

        let mut mins = vec![vec![usize::MAX; bounds]; bounds];
        let mut work_queue = vec![State {
            pos: start,
            dir: Dir::E,
            score: 0,
        }];
        while !work_queue.is_empty() {
            let curr = work_queue.pop().unwrap();
            if curr.score > mins[curr.pos.0][curr.pos.1] {
                continue; // abort this path
            } else {
                mins[curr.pos.0][curr.pos.1] = curr.score;
            }
            if curr.pos == end {
                continue;
            }

            //up
            if curr.pos.0 > 0 && grid[curr.pos.0 - 1][curr.pos.1] != '#' {
                let score = match curr.dir {
                    Dir::N => curr.score + 1,
                    Dir::S => curr.score + 2000 + 1,
                    Dir::E | Dir::W => curr.score + 1000 + 1,
                };
                work_queue.push(State {
                    pos: (curr.pos.0 - 1, curr.pos.1),
                    dir: Dir::N,
                    score,
                });
            }
            //down
            if curr.pos.0 < (bounds - 1) && grid[curr.pos.0 + 1][curr.pos.1] != '#' {
                let score = match curr.dir {
                    Dir::N => curr.score + 2000 + 1,
                    Dir::S => curr.score + 1,
                    Dir::E | Dir::W => curr.score + 1000 + 1,
                };
                work_queue.push(State {
                    pos: (curr.pos.0 + 1, curr.pos.1),
                    dir: Dir::S,
                    score,
                });
            }
            //left
            if curr.pos.1 > 0 && grid[curr.pos.0][curr.pos.1 - 1] != '#' {
                let score = match curr.dir {
                    Dir::N | Dir::S => curr.score + 1000 + 1,
                    Dir::E => curr.score + 1,
                    Dir::W => curr.score + 2000 + 1,
                };
                work_queue.push(State {
                    pos: (curr.pos.0, curr.pos.1 - 1),
                    dir: Dir::E,
                    score,
                });
            }

            //right
            if curr.pos.1 < bounds - 1 && grid[curr.pos.0][curr.pos.1 + 1] != '#' {
                let score = match curr.dir {
                    Dir::N | Dir::S => curr.score + 1000 + 1,
                    Dir::E => curr.score + 2000 + 1,
                    Dir::W => curr.score + 1,
                };
                work_queue.push(State {
                    pos: (curr.pos.0, curr.pos.1 + 1),
                    dir: Dir::W,
                    score,
                });
            }
        }

        let answer = mins[end.0][end.1];
        Ok((answer, mins))
    }

    assert_eq!(7036, part1(BufReader::new(TEST.as_bytes()))?.0);
    assert_eq!(11048, part1(BufReader::new(TEST2.as_bytes()))?.0);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let (result, _) = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R, mins: Vec<Vec<usize>>) -> Result<usize> {
        let grid = read_to_2d(reader);
        let bounds = grid.len();
        let start = (bounds - 2, 1_usize);
        let end = (1_usize, bounds - 2);
        let best_score = mins[end.0][end.1];

        let mut path = HashSet::new();
        path.insert(start);

        let mut work_queue = vec![State2 {
            pos: start,
            dir: Dir::E,
            score: 0,
            path,
        }];
        let mut cells = HashSet::new();

        while !work_queue.is_empty() {
            let curr = work_queue.pop().unwrap();
            if curr.score > (mins[curr.pos.0][curr.pos.1] + 1000) {
                continue; // abort this path
            }
            if curr.pos == end {
                if curr.score == best_score {
                    cells.extend(curr.path.iter().cloned());
                }
                continue;
            }

            //up
            if curr.pos.0 > 0 && grid[curr.pos.0 - 1][curr.pos.1] != '#' {
                let score = match curr.dir {
                    Dir::N => curr.score + 1,
                    Dir::S => curr.score + 2000 + 1,
                    Dir::E | Dir::W => curr.score + 1000 + 1,
                };
                let mut path = curr.path.clone();
                path.insert((curr.pos.0 - 1, curr.pos.1));
                work_queue.push(State2 {
                    pos: (curr.pos.0 - 1, curr.pos.1),
                    dir: Dir::N,
                    score,
                    path,
                });
            }
            //down
            if curr.pos.0 < (bounds - 1) && grid[curr.pos.0 + 1][curr.pos.1] != '#' {
                let score = match curr.dir {
                    Dir::N => curr.score + 2000 + 1,
                    Dir::S => curr.score + 1,
                    Dir::E | Dir::W => curr.score + 1000 + 1,
                };
                let mut path = curr.path.clone();
                path.insert((curr.pos.0 + 1, curr.pos.1));
                work_queue.push(State2 {
                    pos: (curr.pos.0 + 1, curr.pos.1),
                    dir: Dir::S,
                    score,
                    path,
                });
            }
            //left
            if curr.pos.1 > 0 && grid[curr.pos.0][curr.pos.1 - 1] != '#' {
                let score = match curr.dir {
                    Dir::N | Dir::S => curr.score + 1000 + 1,
                    Dir::E => curr.score + 1,
                    Dir::W => curr.score + 2000 + 1,
                };
                let mut path = curr.path.clone();
                path.insert((curr.pos.0, curr.pos.1 - 1));
                work_queue.push(State2 {
                    pos: (curr.pos.0, curr.pos.1 - 1),
                    dir: Dir::E,
                    score,
                    path,
                });
            }

            //right
            if curr.pos.1 < bounds - 1 && grid[curr.pos.0][curr.pos.1 + 1] != '#' {
                let score = match curr.dir {
                    Dir::N | Dir::S => curr.score + 1000 + 1,
                    Dir::E => curr.score + 2000 + 1,
                    Dir::W => curr.score + 1,
                };
                let mut path = curr.path.clone();
                path.insert((curr.pos.0, curr.pos.1 + 1));
                work_queue.push(State2 {
                    pos: (curr.pos.0, curr.pos.1 + 1),
                    dir: Dir::W,
                    score,
                    path,
                });
            }
        }

        let answer = cells.len();
        Ok(answer)
    }

    let (_, mins) = part1(BufReader::new(TEST.as_bytes()))?;
    assert_eq!(45, part2(BufReader::new(TEST.as_bytes()), mins)?);
    let (_, mins) = part1(BufReader::new(TEST2.as_bytes()))?;
    assert_eq!(64, part2(BufReader::new(TEST2.as_bytes()), mins)?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let (_, mins) = part1(BufReader::new(File::open(INPUT_FILE)?))?;
    let result = time_snippet!(part2(input_file, mins)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
