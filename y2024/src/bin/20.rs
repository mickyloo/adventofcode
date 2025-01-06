use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use std::collections::{HashMap, HashSet};
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::ptr::null;

const DAY: &str = "20"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############";

struct State {
    path: HashSet<(usize, usize)>,
    pos: (usize, usize),
}

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn search(
        grid: &Vec<Vec<char>>,
        start: (usize, usize),
        end: (usize, usize),
        cheat: Option<(usize, usize)>,
    ) -> usize {
        let bounds = grid.len();
        let mut path = HashSet::new();
        path.insert(start);
        let mut work_queue = vec![State { pos: start, path }];
        let mut length = usize::MAX;

        let grid = match cheat {
            None => grid,
            Some(cheat) => &{
                let mut grid = grid.clone();
                grid[cheat.0][cheat.1] = '.';
                grid
            },
        };

        while !work_queue.is_empty() {
            let curr = work_queue.pop().unwrap();
            if curr.pos == end {
                if curr.path.len() < length {
                    length = curr.path.len();
                }
                continue;
            }

            //up
            if curr.pos.0 > 0 {
                let next = (curr.pos.0 - 1, curr.pos.1);
                if grid[next.0][next.1] != '#' && !curr.path.contains(&next) {
                    let mut path = curr.path.clone();
                    path.insert(next);
                    work_queue.push(State { pos: next, path });
                }
            }
            //down
            if curr.pos.0 < (bounds - 1) {
                let next = (curr.pos.0 + 1, curr.pos.1);
                if grid[next.0][next.1] != '#' && !curr.path.contains(&next) {
                    let mut path = curr.path.clone();
                    path.insert(next);
                    work_queue.push(State { pos: next, path });
                }
            }
            //left
            if curr.pos.1 > 0 {
                let next = (curr.pos.0, curr.pos.1 - 1);
                if grid[next.0][next.1] != '#' && !curr.path.contains(&next) {
                    let mut path = curr.path.clone();
                    path.insert(next);
                    work_queue.push(State { pos: next, path });
                }
            }
            //right
            if curr.pos.1 < bounds - 1 {
                let next = (curr.pos.0, curr.pos.1 + 1);
                if grid[next.0][next.1] != '#' && !curr.path.contains(&next) {
                    let mut path = curr.path.clone();
                    path.insert(next);
                    work_queue.push(State { pos: next, path });
                }
            }
        }

        length - 1
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        let grid = read_to_2d(reader);
        let start = find(&grid, 'S');
        let end = find(&grid, 'E');

        let baseline = search(&grid, start, end, None);
        let mut counter: HashMap<usize, usize> = HashMap::new();

        for row in 1..grid.len() - 1 {
            for col in 1..grid.len() - 1 {
                if grid[row][col] == '#' {
                    let cheat_score = search(&grid, start, end, Option::from((row, col)));
                    let diff = baseline - cheat_score;
                    if diff > 0 {
                        *counter.entry(diff).or_insert(0) += 1
                    }
                }
            }
        }

        let answer = counter
            .into_iter()
            .filter(|(k, _)| *k >= 100)
            .map(|(_, v)| v)
            .sum();

        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(0, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
